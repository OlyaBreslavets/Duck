package autotests.tests;

import autotests.clients.DuckActionsClient;
import autotests.payloads.DefaultResponseProperties;
import autotests.payloads.DuckProperties;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.http.client.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class DuckActionsTest extends DuckActionsClient {

    @Autowired
    protected HttpClient duckService;

    //Тест-кейсы для /api/duck/action/fly
    @Test(description = "Проверка, что уточка полетела. Положение крыльев - ACTIVE")
    @CitrusTest
    public void successfulFlyActive(@Optional @CitrusResource TestCaseRunner runner) {
        try {
            DuckProperties duckProperties = new DuckProperties()
                    .color("yellow")
                    .height(10.0)
                    .material("plastic")
                    .sound("quak")
                    .wingsState(DuckProperties.WingsState.ACTIVE);
            duckCreatePayload(runner, duckProperties);
            runner.$(http().client(duckService)
                    .receive()
                    .response()
                    .message()
                    .extract(fromBody().expression("$.id", "duckId")));
            duckFly(runner, "${duckId}");
            DefaultResponseProperties defaultResponseProperties = new DefaultResponseProperties().message("I'm flying");
            validateResponsePayload(runner, defaultResponseProperties);
        } finally {
            duckDelete(runner, "${duckId}");
        }
    }

    @Test(description = "Проверка, что уточка не полетела. Положение крыльев - FIXED")
    @CitrusTest
    public void successfulFlyFixed(@Optional @CitrusResource TestCaseRunner runner) {
        try {
            DuckProperties duckProperties = new DuckProperties()
                    .color("yellow")
                    .height(10.0)
                    .material("plastic")
                    .sound("quak")
                    .wingsState(DuckProperties.WingsState.FIXED);
            duckCreatePayload(runner, duckProperties);
            runner.$(http().client(duckService)
                    .receive()
                    .response()
                    .message()
                    .extract(fromBody().expression("$.id", "duckId")));
            duckFly(runner, "${duckId}");
            DefaultResponseProperties defaultResponseProperties = new DefaultResponseProperties().message("I can't fly");
            validateResponsePayload(runner, defaultResponseProperties);
        } finally {
            duckDelete(runner, "${duckId}");
        }
    }

    //Тест-кейсы для /api/duck/action/properties
    @Test(description = "Показать характеристики уточки, где положение крыльев - ACTIVE")
    @CitrusTest
    public void successfulPropertiesActive(@Optional @CitrusResource TestCaseRunner runner) {
        try{
            duckCreateString(runner, "red", "11", "rubber", "quak", "ACTIVE");
            runner.$(http().client(duckService)
                    .receive()
                    .response()
                    .message()
                    .extract(fromBody().expression("$.id", "duckId")));
            duckProperties(runner, "${duckId}");
            validateResponseResource(runner, "duckCreateTest/duckCreateResponse.json");
        } finally {
            duckDelete(runner, "${duckId}");
        }
    }

    @Test(description = "Показать характеристики уточки, где положение крыльев - FIXED")
    @CitrusTest
    public void successfulPropertiesFixed(@Optional @CitrusResource TestCaseRunner runner) {
        try{
            duckCreateString(runner, "red", "11", "rubber", "quak", "FIXED");
            runner.$(http().client(duckService)
                    .receive()
                    .response()
                    .message()
                    .extract(fromBody().expression("$.id", "duckId")));
            duckProperties(runner, "${duckId}");
            validateResponseString(runner, "{\n" +
                    "  \"color\": \"red\",\n" +
                    "  \"height\": 11.0,\n" +
                    "  \"material\": \"rubber\",\n" +
                    "  \"sound\": \"quak\",\n" +
                    "  \"wingsState\": \"FIXED\"\n" +
                    "}");
        } finally {
            duckDelete(runner, "${duckId}");
        }
    }

    @Test(description = "Показать характеристики уточки, где положение крыльев - UNDEFINED")
    @CitrusTest
    public void successfulPropertiesUndefined(@Optional @CitrusResource TestCaseRunner runner) {
        try{
            duckCreateString(runner, "red", "11", "rubber", "quak", "UNDEFINED");
            runner.$(http().client(duckService)
                    .receive()
                    .response()
                    .message()
                    .extract(fromBody().expression("$.id", "duckId")));
            duckProperties(runner, "${duckId}");
            validateResponseString(runner, "{\n" +
                    "  \"color\": \"red\",\n" +
                    "  \"height\": 11.0,\n" +
                    "  \"material\": \"rubber\",\n" +
                    "  \"sound\": \"quak\",\n" +
                    "  \"wingsState\": \"UNDEFINED\"\n" +
                    "}");
        } finally {
            duckDelete(runner, "${duckId}");
        }
    }

    @Test(description = "Показать характеристики уточки, где материал не rubber")
    @CitrusTest
    public void successfulPropertiesNotRubber(@Optional @CitrusResource TestCaseRunner runner) {
        try{
            duckCreateString(runner, "red", "11", "plastic", "quak", "ACTIVE");
            runner.$(http().client(duckService)
                    .receive()
                    .response()
                    .message()
                    .extract(fromBody().expression("$.id", "duckId")));
            duckProperties(runner, "${duckId}");
            validateResponseString(runner, "{\n" +
                    "  \"color\": \"red\",\n" +
                    "  \"height\": 11.0,\n" +
                    "  \"material\": \"plastic\",\n" +
                    "  \"sound\": \"quak\",\n" +
                    "  \"wingsState\": \"ACTIVE\"\n" +
                    "}");
        } finally {
            duckDelete(runner, "${duckId}");
        }
    }

    //Тест-кейсы для /api/duck/action/quak
    @Test(description = "Проверка голоса уточки. Кол-во повторов - 0, кол-во кряков в звуке - 2")
    @CitrusTest
    public void successfulQuakOptionOne(@Optional @CitrusResource TestCaseRunner runner) {
        try{
            duckCreateString(runner, "red", "11", "rubber", "quak", "ACTIVE");
            runner.$(http().client(duckService)
                    .receive()
                    .response()
                    .message()
                    .extract(fromBody().expression("$.id", "duckId")));
            duckQuack(runner, "${duckId}", "0", "2");
            validateResponseString(runner, "{\n" +
                    "  \"sound\": \"\"\n" +
                    "}");
        } finally {
            duckDelete(runner, "${duckId}");
        }
    }

    @Test(description = "Проверка голоса уточки. Кол-во повторов - 3, кол-во кряков в звуке - 2")
    @CitrusTest
    public void successfulQuakOptionTwo(@Optional @CitrusResource TestCaseRunner runner) {
        try{
            duckCreateString(runner, "red", "11", "rubber", "quak", "ACTIVE");
            runner.$(http().client(duckService)
                    .receive()
                    .response()
                    .message()
                    .extract(fromBody().expression("$.id", "duckId")));
            duckQuack(runner, "${duckId}", "3", "2");
            validateResponseString(runner, "{\n" +
                    "  \"sound\": \"quak-quak, quak-quak, quak-quak\"\n" +
                    "}");
        } finally {
            duckDelete(runner, "${duckId}");
        }
    }

    @Test(description = "Проверка голоса уточки. Кол-во повторов - 2, кол-во кряков в звуке - 3")
    @CitrusTest
    public void successfulQuakOptionThree(@Optional @CitrusResource TestCaseRunner runner) {
        try{
            duckCreateString(runner, "red", "11", "rubber", "quak", "ACTIVE");
            runner.$(http().client(duckService)
                    .receive()
                    .response()
                    .message()
                    .extract(fromBody().expression("$.id", "duckId")));
            duckQuack(runner, "${duckId}", "2", "3");
            validateResponseString(runner, "{\n" +
                    "  \"sound\": \"quak-quak-quak, quak-quak-quak\"\n" +
                    "}");
        } finally {
            duckDelete(runner, "${duckId}");
        }
    }

    @Test(description = "Проверка голоса уточки. Кол-во повторов - 3, кол-во кряков в звуке - 3")
    @CitrusTest
    public void successfulQuakOptionFour(@Optional @CitrusResource TestCaseRunner runner) {
        try{
            duckCreateString(runner, "red", "11", "rubber", "quak", "ACTIVE");
            runner.$(http().client(duckService)
                    .receive()
                    .response()
                    .message()
                    .extract(fromBody().expression("$.id", "duckId")));
            duckQuack(runner, "${duckId}", "3", "3");
            validateResponseString(runner, "{\n" +
                    "  \"sound\": \"quak-quak-quak, quak-quak-quak, quak-quak-quak\"\n" +
                    "}");
        } finally {
            duckDelete(runner, "${duckId}");
        }
    }

    @Test(description = "Проверка голоса уточки. Кол-во повторов - 3, кол-во кряков в звуке - 0")
    @CitrusTest
    public void successfulQuakOptionFive(@Optional @CitrusResource TestCaseRunner runner) {
        try{
            duckCreateString(runner, "red", "11", "rubber", "quak", "ACTIVE");
            runner.$(http().client(duckService)
                    .receive()
                    .response()
                    .message()
                    .extract(fromBody().expression("$.id", "duckId")));
            duckQuack(runner, "${duckId}", "3", "0");
            validateResponseString(runner, "{\n" +
                    "  \"sound\": \"\"\n" +
                    "}");
        } finally {
            duckDelete(runner, "${duckId}");
        }
    }

    //Тест-кейсы для /api/duck/action/swim
    @Test(description = "Проверка, что уточка поплыла")
    @CitrusTest
    public void successfulSwim(@Optional @CitrusResource TestCaseRunner runner) {
        try {
            duckCreateString(runner, "red", "11", "rubber", "quak", "ACTIVE");
            runner.$(http().client(duckService)
                    .receive()
                    .response()
                    .message()
                    .extract(fromBody().expression("$.id", "duckId")));
            duckSwim(runner, "${duckId}");
            validateResponseString(runner, "{\n" +
                    "  \"message\": \"I'm swimming\"\n" +
                    "}");
        } finally {
            duckDelete(runner, "${duckId}");
        }
    }
}
