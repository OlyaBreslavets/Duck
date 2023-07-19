package autotests.clients;

import autotests.EndpointConfig;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.message.builder.ObjectMappingPayloadBuilder;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;

@ContextConfiguration(classes = {EndpointConfig.class})
public class DuckActionsClient extends TestNGCitrusSpringSupport {

    @Autowired
    protected HttpClient duckService;

    public void duckFly(TestCaseRunner runner, String id) {
        runner.$(http().client(duckService)
                .send()
                .get("/api/duck/action/fly")
                .queryParam("id", id));
    }

    public void duckProperties(TestCaseRunner runner, String id) {
        runner.$(http().client(duckService)
                .send()
                .get("/api/duck/action/properties")
                .queryParam("id", id));
    }

    public void duckQuack(TestCaseRunner runner, String id, String repetitionCount, String soundCount) {
        runner.$(http().client(duckService)
                .send()
                .get("/api/duck/action/quack")
                .queryParam("id", id)
                .queryParam("repetitionCount", repetitionCount)
                .queryParam("soundCount", soundCount));
    }

    public void duckSwim(TestCaseRunner runner, String id) {
        runner.$(http().client(duckService)
                .send()
                .get("/api/duck/action/swim")
                .queryParam("id", id));
    }

    @Description("Валидация полученного ответа (String)")
    public void validateResponseString(TestCaseRunner runner, String response) {
        runner.$(http().client(duckService)
                .receive()
                .response(HttpStatus.OK)
                .message().type(MessageType.JSON)
                .body(response));
    }

    @Description("Валидация полученного ответа (из папки resource)")
    public void validateResponseResource(TestCaseRunner runner, String expectedResource) {
        runner.$(http().client(duckService)
                .receive()
                .response(HttpStatus.OK)
                .message().type(MessageType.JSON)
                .body(new ClassPathResource(expectedResource)));
    }

    @Description("Валидация полученного ответа (из Payload)")
    public void validateResponsePayload(TestCaseRunner runner, Object expectedPayload) {
        runner.$(http().client(duckService)
                .receive()
                .response(HttpStatus.OK)
                .message().type(MessageType.JSON)
                .body(new ObjectMappingPayloadBuilder(expectedPayload, new ObjectMapper())));
    }
}
