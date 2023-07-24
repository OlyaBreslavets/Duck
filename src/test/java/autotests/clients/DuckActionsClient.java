package autotests.clients;

import autotests.BaseTest;
import autotests.EndpointConfig;
import com.consol.citrus.TestCaseRunner;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {EndpointConfig.class})
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
        sendPostRequest(runner, duckService, "/api/duck/create",
                "color", color,
                "height", height,
                "material", material,
                "sound", sound,
                "wingsState", wingsState);
    }

    public void duckCreatePayload(TestCaseRunner runner, Object expectedPayload) {
        sendPostRequest(runner, duckService, "/api/duck/create", expectedPayload);
    }

    public void duckDelete(TestCaseRunner runner, String id) {
        sendDeleteRequest(runner, duckService, "/api/duck/delete", "id", id);
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