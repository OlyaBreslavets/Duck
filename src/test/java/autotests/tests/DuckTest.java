package autotests.tests;

import autotests.clients.DuckClient;
import autotests.payloads.DefaultResponseProperties;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.actions.ExecuteSQLAction.Builder.sql;
import static com.consol.citrus.container.FinallySequence.Builder.doFinally;
import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class DuckTest extends DuckClient {

    //Тест-кейсы для /api/duck/action/create
    @Test(description = "Проверка, что уточка создаётся")
    @CitrusTest
    public void successfulCreate(@Optional @CitrusResource TestCaseRunner runner) {
        duckCreateResources(runner, "getDuckPropertiesTest/duckProperties.json");
        validateResponseAndExtractId(runner,
                "{\n" +
                        "  \"id\": \"@ignore@\",\n" +
                        "  \"color\": \"red\",\n" +
                        "  \"height\": 11.0,\n" +
                        "  \"material\": \"rubber\",\n" +
                        "  \"sound\": \"quak\",\n" +
                        "  \"wingsState\": \"ACTIVE\"\n" +
                        "}");
        runner.$(doFinally().actions(runner.$(sql(db).statement("DELETE FROM DUCK WHERE ID=${duckId}"))));
    }

    @Test(description = "Проверка, что уточка создаётся с пустым телом и заполняется значениями по умолчанию")
    @CitrusTest
    public void successfulCreateDefault(@Optional @CitrusResource TestCaseRunner runner) {
        duckCreateResources(runner, "duckCreateTest/duckEmptyBody.json");
        validateResponseAndExtractId(runner,
                "{\n" +
                        "  \"id\": \"@ignore@\",\n" +
                        "  \"color\": \"\",\n" +
                        "  \"height\": 0.0,\n" +
                        "  \"material\": \"\",\n" +
                        "  \"sound\": \"\",\n" +
                        "  \"wingsState\": \"UNDEFINED\"\n" +
                        "}");
        runner.$(doFinally().actions(runner.$(sql(db).statement("DELETE FROM DUCK WHERE ID=${duckId}"))));
    }

    @Test(description = "Проверка, что уточка создаётся (с проверкой данных в БД)")
    @CitrusTest
    public void successfulCreateDB(@Optional @CitrusResource TestCaseRunner runner) {
        duckCreateResources(runner, "getDuckPropertiesTest/duckProperties.json");
        validateResponseAndExtractId(runner,
                "{\n" +
                        "  \"id\": \"@ignore@\",\n" +
                        "  \"color\": \"red\",\n" +
                        "  \"height\": 11.0,\n" +
                        "  \"material\": \"rubber\",\n" +
                        "  \"sound\": \"quak\",\n" +
                        "  \"wingsState\": \"ACTIVE\"\n" +
                        "}");
        validateDuckInDatabase(runner, "${duckId}", "red", "11.0", "rubber", "quak", "ACTIVE");
        runner.$(doFinally().actions(runner.$(sql(db).statement("DELETE FROM DUCK WHERE ID=${duckId}"))));
    }

    //Тест-кейсы для /api/duck/action/delete
    @Test(description = "Проверка, что уточка удаляется (уточка существующая)")
    @CitrusTest
    public void successfulDeleteExist(@Optional @CitrusResource TestCaseRunner runner) {
        duckCreateResources(runner, "getDuckPropertiesTest/duckProperties.json");
        extractId(runner);
        runner.$(doFinally().actions(runner.$(sql(db).statement("DELETE FROM DUCK WHERE ID=${duckId}"))));
        duckDelete(runner, "${duckId}");
        validateResponseString(runner,
                "{\n" +
                        "  \"message\": \"Duck is deleted\"\n" +
                        "}");
    }

    @Test(description = "Проверка, что уточка удаляется (уточка несуществующая)")
    @CitrusTest
    public void successfulDeleteNotExist(@Optional @CitrusResource TestCaseRunner runner) {
        duckCreateResources(runner, "getDuckPropertiesTest/duckProperties.json");
        extractId(runner);
        runner.$(doFinally().actions(runner.$(sql(db).statement("DELETE FROM DUCK WHERE ID=${duckId}"))));
        runner.$(sql(db).statement("DELETE FROM DUCK WHERE ID=${duckId}"));
        duckDelete(runner, "${duckId}");
        validateResponseString(runner,
                "{\n" +
                        "  \"message\": \"Duck with id = ${duckId} is not found\"\n" +
                        "}");
    }

    //Тест-кейсы для /api/duck/action/getAllIds
    @Test(description = "Проверка, что список уточек пуст")
    @CitrusTest
    public void successfulGetAllIdsEmpty(@Optional @CitrusResource TestCaseRunner runner) {
        runner.$(sql(db).statement("TRUNCATE TABLE DUCK"));
        duckGetAllIds(runner);
        validateResponseString(runner, "[]");
    }

    @Test(description = "Проверка, что списка уточек (созданы три уточки")
    @CitrusTest
    public void successfulGetAllIds(@Optional @CitrusResource TestCaseRunner runner) {
        duckCreateResources(runner, "getDuckPropertiesTest/duckProperties.json");
        runner.$(http().client(duckService)
                .receive()
                .response()
                .message()
                .extract(fromBody().expression("$.id", "duckId1")));
        duckCreateResources(runner, "getDuckPropertiesTest/duckProperties.json");
        runner.$(http().client(duckService)
                .receive()
                .response()
                .message()
                .extract(fromBody().expression("$.id", "duckId2")));
        duckCreateResources(runner, "getDuckPropertiesTest/duckProperties.json");
        runner.$(http().client(duckService)
                .receive()
                .response()
                .message()
                .extract(fromBody().expression("$.id", "duckId3")));
        duckGetAllIds(runner);
        validateResponseString(runner,
                "[" +
                        "${duckId1}," +
                        "${duckId2}," +
                        "${duckId3}" +
                        "]");
        runner.$(doFinally().actions(
                runner.$(sql(db).statement("DELETE FROM DUCK WHERE ID=${duckId1}")),
                runner.$(sql(db).statement("DELETE FROM DUCK WHERE ID=${duckId2}")),
                runner.$(sql(db).statement("DELETE FROM DUCK WHERE ID=${duckId3}"))));
    }

    //Тест-кейсы для /api/duck/update
    @Test(description = "Проверка обновления уточки")
    @CitrusTest
    public void successfulUpdate(@Optional @CitrusResource TestCaseRunner runner) {
        DefaultResponseProperties defaultResponseProperties = new DefaultResponseProperties()
                .message("Duck with id = ${duckId} is updated");

        duckCreateResources(runner, "getDuckPropertiesTest/duckProperties.json");
        extractId(runner);
        runner.$(doFinally().actions(runner.$(sql(db).statement("DELETE FROM DUCK WHERE ID=${duckId}"))));
        duckUpdate(runner, "${duckId}", "yellow", "12.0", "plastic", "QUAK", "FIXED");
        validateResponsePayload(runner, defaultResponseProperties);
        validateDuckInDatabase(runner, "${duckId}", "yellow", "12.0", "plastic", "QUAK", "FIXED");
    }
}
