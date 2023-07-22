package autotests.tests;

import autotests.clients.DuckClient;
import autotests.payloads.DefaultResponseProperties;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.http.client.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class DuckTest extends DuckClient {

    @Autowired
    protected HttpClient duckService;

    //Тест-кейсы для /api/duck/action/create
    @Test(description = "Проверка, что уточка создаётся")
    @CitrusTest
    public void successfulCreate(@Optional @CitrusResource TestCaseRunner runner) {
        try {
            duckCreateResources(runner, "duckCreateTest/duckCreateResponse.json");
            validateResponseStringForCreate(runner, "{\n" +
                    "  \"id\": \"@ignore@\",\n" +
                    "  \"color\": \"red\",\n" +
                    "  \"height\": 11.0,\n" +
                    "  \"material\": \"rubber\",\n" +
                    "  \"sound\": \"quak\",\n" +
                    "  \"wingsState\": \"ACTIVE\"\n" +
                    "}");
        } finally {
            duckDelete(runner, "${duckId}");
        }
    }

    @Test(description = "Проверка, что уточка создаётся с пустым телом и заполняется значениями по умолчанию")
    @CitrusTest
    public void successfulCreateDefault(@Optional @CitrusResource TestCaseRunner runner) {
        try {
            duckCreateResources(runner, "duckCreateTest/duckCreateResponseEmptyBody.json");
            validateResponseStringForCreate(runner, "{\n" +
                    "  \"id\": \"@ignore@\",\n" +
                    "  \"color\": \"\",\n" +
                    "  \"height\": 0.0,\n" +
                    "  \"material\": \"\",\n" +
                    "  \"sound\": \"\",\n" +
                    "  \"wingsState\": \"UNDEFINED\"\n" +
                    "}");
        } finally {
            duckDelete(runner, "${duckId}");
        }
    }

    //Тест-кейсы для /api/duck/action/delete
    @Test(description = "Проверка, что уточка удаляется (уточка существующая)")
    @CitrusTest
    public void successfulDeleteExist(@Optional @CitrusResource TestCaseRunner runner) {
        duckCreateResources(runner, "duckCreateTest/duckCreateResponse.json");
        runner.$(http().client(duckService)
                .receive()
                .response()
                .message()
                .extract(fromBody().expression("$.id", "duckId")));
        duckDelete(runner, "${duckId}");
        validateResponseString(runner, "{\n" +
                "  \"message\": \"Duck is deleted\"\n" +
                "}");
    }

    @Test(description = "Проверка, что уточка удаляется (уточка несуществующая)")
    @CitrusTest
    public void successfulDeleteNotExist(@Optional @CitrusResource TestCaseRunner runner) {
        duckCreateResources(runner, "duckCreateTest/duckCreateResponse.json");
        runner.$(http().client(duckService)
                .receive()
                .response()
                .message()
                .extract(fromBody().expression("$.id", "duckId")));
        duckDelete(runner, "${duckId}");
        duckDelete(runner, "${duckId}");
        validateResponseString(runner, "{\n" +
                "  \"message\": \"Duck with id = ${duckId} is not found\"\n" +
                "}");
    }

    //Тест-кейсы для /api/duck/action/getAllIds
    @Test(description = "Проверка, что список уточек пуст (таблица duck пустая, предварительно выполнить команду: TRUNCATE TABLE DUCK)")
    @CitrusTest
    public void successfulGetAllIdsEmpty(@Optional @CitrusResource TestCaseRunner runner) {
        runner.$(http().client(duckService)
                .send()
                .get("/api/duck/getAllIds"));
        validateResponseString(runner, "[]");
    }

    @Test(description = "Проверка, что списка уточек (созданы три уточки")
    @CitrusTest
    public void successfulGetAllIds(@Optional @CitrusResource TestCaseRunner runner) {
        try {
            duckCreateResources(runner, "duckCreateTest/duckCreateResponse.json");
            runner.$(http().client(duckService)
                    .receive()
                    .response()
                    .message()
                    .extract(fromBody().expression("$.id", "duckId1")));
            duckCreateResources(runner, "duckCreateTest/duckCreateResponse.json");
            runner.$(http().client(duckService)
                    .receive()
                    .response()
                    .message()
                    .extract(fromBody().expression("$.id", "duckId2")));
            duckCreateResources(runner, "duckCreateTest/duckCreateResponse.json");
            runner.$(http().client(duckService)
                    .receive()
                    .response()
                    .message()
                    .extract(fromBody().expression("$.id", "duckId3")));
            runner.$(http().client(duckService)
                    .send()
                    .get("/api/duck/getAllIds"));
            validateResponseString(runner, "[" +
                    "${duckId1}," +
                    "${duckId2}," +
                    "${duckId3}" +
                    "]");
        } finally {
            duckDelete(runner, "${duckId1}");
            duckDelete(runner, "${duckId2}");
            duckDelete(runner, "${duckId3}");
        }
    }

    //Тест-кейсы для /api/duck/update
    @Test(description = "Проверка обновления уточки")
    @CitrusTest
    public void successfulUpdate(@Optional @CitrusResource TestCaseRunner runner) {
        try {
            duckCreateResources(runner, "duckCreateTest/duckCreateResponse.json");
            runner.$(http().client(duckService)
                    .receive()
                    .response()
                    .message()
                    .extract(fromBody().expression("$.id", "duckId")));
            duckUpdate(runner, "${duckId}", "yellow", "12", "plastic", "QUAK", "FIXED");
            DefaultResponseProperties defaultResponseProperties = new DefaultResponseProperties()
                    .message("Duck with id = ${duckId} is updated");
            validateResponsePayload(runner, defaultResponseProperties);
        } finally {
            duckDelete(runner, "${duckId}");
        }
    }
}
