package autotests.clients;

import autotests.BaseTest;
import autotests.EndpointConfig;
import com.consol.citrus.TestCaseRunner;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {EndpointConfig.class})
public class DuckClient extends BaseTest {

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

    @Description("Валидация полученного ответа (String)")
    public void validateResponseString(TestCaseRunner runner, String response) {
        validateResponse(runner, duckService, HttpStatus.OK, response);
    }

    @Description("Валидация полученного ответа DUCK (String)")
    public void validateResponseResourceForCreate(TestCaseRunner runner, String response) {
        validateResponseResourceForCreate(runner, duckService, HttpStatus.OK, response);
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