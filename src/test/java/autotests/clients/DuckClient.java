package autotests.clients;

import autotests.BaseTest;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.message.MessageType;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpStatus;

import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class DuckClient extends BaseTest {

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

    public void duckCreateResources(TestCaseRunner runner, String expectedResource) {
        sendPostRequest(runner, duckService, "/api/duck/create", expectedResource);
    }

    public void duckDelete(TestCaseRunner runner, String id) {
        sendDeleteRequest(runner, duckService, "/api/duck/delete", "id", id);
    }

    public void duckUpdate(TestCaseRunner runner,
                           String id,
                           String color,
                           String height,
                           String material,
                           String sound,
                           String wingsState) {
        sendPutRequest(runner, duckService, "/api/duck/update",
                "id", id,
                "color", color,
                "height", height,
                "material", material,
                "sound", sound,
                "wingsState", wingsState);
    }

    public void duckGetAllIds(TestCaseRunner runner) {
        sendGetRequest(runner, duckService, "/api/duck/getAllIds");
    }

    public void extractId(TestCaseRunner runner) {
        runner.$(http().client(duckService)
                .receive()
                .response()
                .message()
                .extract(fromBody().expression("$.id", "duckId")));
    }

    @Description("Валидация полученного ответа (String)")
    public void validateResponseString(TestCaseRunner runner, String response) {
        validateResponse(runner, duckService, HttpStatus.OK, response);
    }

    @Description("Валидация полученного ответа с записью id в переменную (String)")
    public void validateResponseAndExtractId(TestCaseRunner runner, String response) {
        runner.$(http().client(duckService)
                .receive()
                .response(HttpStatus.OK)
                .message().type(MessageType.JSON)
                .body(response)
                .extract(fromBody().expression("$.id", "duckId")));
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