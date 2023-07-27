package autotests.tests;

import autotests.clients.DuckActionsClient;
import autotests.payloads.DefaultResponseProperties;
import autotests.payloads.DuckProperties;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.actions.ExecuteSQLAction.Builder.sql;
import static com.consol.citrus.actions.ExecuteSQLQueryAction.Builder.query;
import static com.consol.citrus.container.FinallySequence.Builder.doFinally;

public class DuckActionsTest extends DuckActionsClient {

    //Тест-кейсы для /api/duck/action/fly
    @Test(description = "Проверка, что уточка полетела. Положение крыльев - ACTIVE")
    @CitrusTest
    public void successfulFlyActive(@Optional @CitrusResource TestCaseRunner runner) {
        DuckProperties duckProperties = new DuckProperties()
                .color("yellow")
                .height(10.0)
                .material("plastic")
                .sound("quak")
                .wingsState(DuckProperties.WingsState.ACTIVE);
        DefaultResponseProperties defaultResponseProperties = new DefaultResponseProperties().message("I'm flying");

        duckCreatePayload(runner, duckProperties);
        extractId(runner);
        runner.$(doFinally().actions(runner.$(sql(db).statement("DELETE FROM DUCK WHERE ID=${duckId}"))));
        duckFly(runner, "${duckId}");
        validateResponsePayload(runner, defaultResponseProperties);
    }

    @Test(description = "Проверка, что уточка не полетела. Положение крыльев - FIXED")
    @CitrusTest
    public void successfulFlyFixed(@Optional @CitrusResource TestCaseRunner runner) {
        DuckProperties duckProperties = new DuckProperties()
                .color("yellow")
                .height(10.0)
                .material("plastic")
                .sound("quak")
                .wingsState(DuckProperties.WingsState.FIXED);
        DefaultResponseProperties defaultResponseProperties = new DefaultResponseProperties().message("I can't fly");

        duckCreatePayload(runner, duckProperties);
        extractId(runner);
        runner.$(doFinally().actions(runner.$(sql(db).statement("DELETE FROM DUCK WHERE ID=${duckId}"))));
        duckFly(runner, "${duckId}");
        validateResponsePayload(runner, defaultResponseProperties);
    }

    //Тест-кейсы для /api/duck/action/properties
    @Test(description = "Показать характеристики уточки, где положение крыльев - ACTIVE")
    @CitrusTest
    public void successfulPropertiesActive(@Optional @CitrusResource TestCaseRunner runner) {
        DuckProperties duckProperties = new DuckProperties()
                .color("red")
                .height(11.0)
                .material("rubber")
                .sound("quak")
                .wingsState(DuckProperties.WingsState.ACTIVE);

        duckCreatePayload(runner, duckProperties);
        extractId(runner);
        duckProperties(runner, "${duckId}");
        validateResponsePayload(runner, duckProperties);
        runner.$(doFinally().actions(runner.$(sql(db).statement("DELETE FROM DUCK WHERE ID=${duckId}"))));
    }

    @Test(description = "Показать характеристики уточки, где положение крыльев - FIXED")
    @CitrusTest
    public void successfulPropertiesFixed(@Optional @CitrusResource TestCaseRunner runner) {
        DuckProperties duckProperties = new DuckProperties()
                .color("red")
                .height(11.0)
                .material("rubber")
                .sound("quak")
                .wingsState(DuckProperties.WingsState.FIXED);

        duckCreatePayload(runner, duckProperties);
        extractId(runner);
        duckProperties(runner, "${duckId}");
        validateResponsePayload(runner, duckProperties);
        runner.$(doFinally().actions(runner.$(sql(db).statement("DELETE FROM DUCK WHERE ID=${duckId}"))));
    }

    @Test(description = "Показать характеристики уточки, где положение крыльев - UNDEFINED")
    @CitrusTest
    public void successfulPropertiesUndefined(@Optional @CitrusResource TestCaseRunner runner) {
        DuckProperties duckProperties = new DuckProperties()
                .color("red")
                .height(11.0)
                .material("rubber")
                .sound("quak")
                .wingsState(DuckProperties.WingsState.UNDEFINED);

        duckCreatePayload(runner, duckProperties);
        extractId(runner);
        duckProperties(runner, "${duckId}");
        validateResponsePayload(runner, duckProperties);
        runner.$(doFinally().actions(runner.$(sql(db).statement("DELETE FROM DUCK WHERE ID=${duckId}"))));
    }

    @Test(description = "Показать характеристики уточки, где материал не rubber")
    @CitrusTest
    public void successfulPropertiesNotRubber(@Optional @CitrusResource TestCaseRunner runner) {
        DuckProperties duckProperties = new DuckProperties()
                .color("red")
                .height(11.0)
                .material("plastic")
                .sound("quak")
                .wingsState(DuckProperties.WingsState.ACTIVE);

        duckCreatePayload(runner, duckProperties);
        extractId(runner);
        duckProperties(runner, "${duckId}");
        validateResponsePayload(runner, duckProperties);
        runner.$(doFinally().actions(runner.$(sql(db).statement("DELETE FROM DUCK WHERE ID=${duckId}"))));
    }

    //Тест-кейсы для /api/duck/action/quak
    @Test(description = "Проверка голоса уточки. Кол-во повторов - 0, кол-во кряков в звуке - 2")
    @CitrusTest
    public void successfulQuakOptionOne(@Optional @CitrusResource TestCaseRunner runner) {
        duckCreateString(runner, "red", "11", "rubber", "quak", "ACTIVE");
        extractId(runner);
        runner.$(doFinally().actions(runner.$(sql(db).statement("DELETE FROM DUCK WHERE ID=${duckId}"))));
        duckQuack(runner, "${duckId}", "0", "2");
        validateResponseString(runner,
                "{\n" +
                        "  \"sound\": \"\"\n" +
                        "}");
    }

    @Test(description = "Проверка голоса уточки. Кол-во повторов - 3, кол-во кряков в звуке - 2")
    @CitrusTest
    public void successfulQuakOptionTwo(@Optional @CitrusResource TestCaseRunner runner) {
        duckCreateString(runner, "red", "11", "rubber", "quak", "ACTIVE");
        extractId(runner);
        runner.$(doFinally().actions(runner.$(sql(db).statement("DELETE FROM DUCK WHERE ID=${duckId}"))));
        duckQuack(runner, "${duckId}", "3", "2");
        validateResponseString(runner,
                "{\n" +
                        "  \"sound\": \"quak-quak, quak-quak, quak-quak\"\n" +
                        "}");
    }

    @Test(description = "Проверка голоса уточки. Кол-во повторов - 2, кол-во кряков в звуке - 3")
    @CitrusTest
    public void successfulQuakOptionThree(@Optional @CitrusResource TestCaseRunner runner) {
        duckCreateString(runner, "red", "11", "rubber", "quak", "ACTIVE");
        extractId(runner);
        runner.$(doFinally().actions(runner.$(sql(db).statement("DELETE FROM DUCK WHERE ID=${duckId}"))));
        duckQuack(runner, "${duckId}", "2", "3");
        validateResponseString(runner,
                "{\n" +
                        "  \"sound\": \"quak-quak-quak, quak-quak-quak\"\n" +
                        "}");
    }

    @Test(description = "Проверка голоса уточки. Кол-во повторов - 3, кол-во кряков в звуке - 3")
    @CitrusTest
    public void successfulQuakOptionFour(@Optional @CitrusResource TestCaseRunner runner) {
        duckCreateString(runner, "red", "11", "rubber", "quak", "ACTIVE");
        extractId(runner);
        runner.$(doFinally().actions(runner.$(sql(db).statement("DELETE FROM DUCK WHERE ID=${duckId}"))));
        duckQuack(runner, "${duckId}", "3", "3");
        validateResponseString(runner,
                "{\n" +
                        "  \"sound\": \"quak-quak-quak, quak-quak-quak, quak-quak-quak\"\n" +
                        "}");
    }

    @Test(description = "Проверка голоса уточки. Кол-во повторов - 3, кол-во кряков в звуке - 3")
    @CitrusTest
    public void successfulQuakOptionFourDB(@Optional @CitrusResource TestCaseRunner runner) {
        runner.$(query(db)
                .statement("SELECT max(id)+1 ID FROM DUCK")
                .extract("ID", "duckId"));
        runner.$(doFinally().actions(runner.$(sql(db).statement("DELETE FROM DUCK WHERE ID=${duckId}"))));
        databaseUpdate(runner,
                "INSERT INTO DUCK (id, color, height, material, sound, wings_state)\n" +
                        "VALUES (${duckId}, 'orange', 10.0, 'rubber', 'quak','ACTIVE');");
        duckQuack(runner, "${duckId}", "3", "3");
        validateResponseString(runner,
                "{\n" +
                        "  \"sound\": \"quak-quak-quak, quak-quak-quak, quak-quak-quak\"\n" +
                        "}");
    }

    @Test(description = "Проверка голоса уточки. Кол-во повторов - 3, кол-во кряков в звуке - 0")
    @CitrusTest
    public void successfulQuakOptionFive(@Optional @CitrusResource TestCaseRunner runner) {
        duckCreateString(runner, "red", "11", "rubber", "quak", "ACTIVE");
        extractId(runner);
        runner.$(doFinally().actions(runner.$(sql(db).statement("DELETE FROM DUCK WHERE ID=${duckId}"))));
        duckQuack(runner, "${duckId}", "3", "0");
        validateResponseString(runner,
                "{\n" +
                        "  \"sound\": \"\"\n" +
                        "}");
    }

    //Тест-кейсы для /api/duck/action/swim
    @Test(description = "Проверка, что уточка поплыла")
    @CitrusTest
    public void successfulSwim(@Optional @CitrusResource TestCaseRunner runner) {
        duckCreateString(runner, "red", "11", "rubber", "quak", "ACTIVE");
        extractId(runner);
        runner.$(doFinally().actions(runner.$(sql(db).statement("DELETE FROM DUCK WHERE ID=${duckId}"))));
        duckSwim(runner, "${duckId}");
        validateResponseString(runner,
                "{\n" +
                        "  \"message\": \"I'm swimming\"\n" +
                        "}");
    }
}
