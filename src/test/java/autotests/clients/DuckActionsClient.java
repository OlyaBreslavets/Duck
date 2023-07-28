package autotests.clients;

import autotests.BaseTest;
import com.consol.citrus.TestCaseRunner;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpStatus;

import static com.consol.citrus.actions.ExecuteSQLAction.Builder.sql;
import static com.consol.citrus.container.FinallySequence.Builder.doFinally;
import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class DuckActionsClient extends BaseTest {

    public void duckFly(TestCaseRunner runner, String id) {
        sendGetRequest(runner, duckService, "/api/duck/action/fly", "id", id);
    }

    public void duckProperties(TestCaseRunner runner, String id) {
        sendGetRequest(runner, duckService, "/api/duck/action/properties", "id", id);
    }

    public void duckQuack(TestCaseRunner runner, String id, String repetitionCount, String soundCount) {
        sendGetRequest(runner,
                duckService,
                "/api/duck/action/quack",
                "id", id,
                "repetitionCount", repetitionCount,
                "soundCount", soundCount);
    }

    public void duckSwim(TestCaseRunner runner, String id) {
        sendGetRequest(runner, duckService, "/api/duck/action/swim", "id", id);
    }

    public void duckCreateString(TestCaseRunner runner,
                                 String color,
                                 String height,
                                 String material,
                                 String sound,
                                 String wingsState) {
        sendPostRequestString(runner, duckService, "/api/duck/create",
                "{\n" +
                        "  \"color\": \"" + color + "\",\n" +
                        "  \"height\": " + height + ",\n" +
                        "  \"material\": \"" + material + "\",\n" +
                        "  \"sound\": \"" + sound + "\",\n" +
                        "  \"wingsState\": \"" + wingsState + "\"\n" +
                        "}");
    }

    public void duckCreatePayload(TestCaseRunner runner, Object expectedPayload) {
        sendPostRequest(runner, duckService, "/api/duck/create", expectedPayload);
    }

    public void extractId(TestCaseRunner runner) {
        runner.$(http().client(duckService)
                .receive()
                .response()
                .message()
                .extract(fromBody().expression("$.id", "duckId")));
    }

    public void deleteFinally(TestCaseRunner runner) {
        runner.$(doFinally().actions((sql(db).statement("DELETE FROM DUCK WHERE ID=${duckId}"))));
    }

    @Description("Валидация полученного ответа (String)")
    public void validateResponseString(TestCaseRunner runner, String response) {
        validateResponse(runner, duckService, HttpStatus.OK, response);
    }

    @Description("Валидация полученного ответа (из папки resource)")
    public void validateResponseResource(TestCaseRunner runner, String expectedResource) {
        validateResponseResource(runner, duckService, HttpStatus.OK, expectedResource);
    }

    @Description("Валидация полученного ответа (из Payload)")
    public void validateResponsePayload(TestCaseRunner runner, Object expectedPayload) {
        validateResponse(runner, duckService, HttpStatus.OK, expectedPayload);
    }
}