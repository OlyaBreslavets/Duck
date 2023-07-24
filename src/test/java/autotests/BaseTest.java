package autotests;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.message.builder.ObjectMappingPayloadBuilder;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

@ContextConfiguration(classes = {EndpointConfig.class})
public class BaseTest extends TestNGCitrusSpringSupport {

    @Autowired
    protected HttpClient duckService;

    protected void sendGetRequest(TestCaseRunner runner,
                                  HttpClient URL,
                                  String path,
                                  String queryName,
                                  String queryValue) {
        runner.$(http().client(URL)
                .send()
                .get(path)
                .queryParam(queryName, queryValue));
    }

    protected void sendGetRequest(TestCaseRunner runner,
                                  HttpClient URL,
                                  String path,
                                  String queryName1,
                                  String queryValue1,
                                  String queryName2,
                                  String queryValue2,
                                  String queryName3,
                                  String queryValue3) {
        runner.$(http().client(URL)
                .send()
                .get(path)
                .queryParam(queryName1, queryValue1)
                .queryParam(queryName2, queryValue2)
                .queryParam(queryName3, queryValue3));
    }

    protected void sendGetRequest(TestCaseRunner runner, HttpClient URL, String path) {
        runner.$(http().client(URL)
                .send()
                .get(path));
    }

    protected void sendPostRequest(TestCaseRunner runner,
                                   HttpClient URL,
                                   String path,
                                   String queryName1, String queryValue1,
                                   String queryName2, String queryValue2,
                                   String queryName3, String queryValue3,
                                   String queryName4, String queryValue4,
                                   String queryName5, String queryValue5) {
        runner.$(http().client(URL)
                .send()
                .post(path)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("{\n" +
                        "  \"" + queryName1 + "\": \"" + queryValue1 + "\",\n" +
                        "  \"" + queryName2 + "\": " + queryValue2 + ",\n" +
                        "  \"" + queryName3 + "\": \"" + queryValue3 + "\",\n" +
                        "  \"" + queryName4 + "\": \"" + queryValue4 + "\",\n" +
                        "  \"" + queryName5 + "\": \"" + queryValue5 + "\"\n" +
                        "}"));
    }

    protected void sendPostRequest(TestCaseRunner runner, HttpClient URL, String path, Object expectedPayload) {
        runner.$(http().client(URL)
                .send()
                .post(path)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ObjectMappingPayloadBuilder(expectedPayload, new ObjectMapper())));
    }

    protected void sendPostRequest(TestCaseRunner runner, HttpClient URL, String path, String expectedResource) {
        runner.$(http().client(URL)
                .send()
                .post(path)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ClassPathResource(expectedResource)));
    }

    protected void sendDeleteRequest(TestCaseRunner runner, HttpClient URL, String path,
                                     String queryName,
                                     String queryValue) {
        runner.$(http().client(URL)
                .send()
                .delete(path)
                .queryParam(queryName, queryValue));
    }

    protected void sendPutRequest(TestCaseRunner runner,
                                  HttpClient URL,
                                  String path,
                                  String queryName1, String queryValue1,
                                  String queryName2, String queryValue2,
                                  String queryName3, String queryValue3,
                                  String queryName4, String queryValue4,
                                  String queryName5, String queryValue5,
                                  String queryName6, String queryValue6) {
        runner.$(http().client(URL)
                .send()
                .put(path)
                .queryParam(queryName1, queryValue1)
                .queryParam(queryName2, queryValue2)
                .queryParam(queryName3, queryValue3)
                .queryParam(queryName4, queryValue4)
                .queryParam(queryName5, queryValue5)
                .queryParam(queryName6, queryValue6));
    }

    protected void validateResponse(TestCaseRunner runner,
                                    HttpClient URL,
                                    HttpStatus status,
                                    String response) {
        runner.$(http().client(URL)
                .receive()
                .response(status)
                .message().type(MessageType.JSON)
                .body(response));
    }

    protected void validateResponseResourceForCreate(TestCaseRunner runner,
                                                     HttpClient URL,
                                                     HttpStatus status,
                                                     String response) {
        runner.$(http().client(URL)
                .receive()
                .response(status)
                .message().type(MessageType.JSON)
                .body(response)
                .extract(fromBody().expression("$.id", "duckId")));
    }

    protected void validateResponseResource(TestCaseRunner runner,
                                            HttpClient URL,
                                            HttpStatus status,
                                            String expectedResource) {
        runner.$(http().client(URL)
                .receive()
                .response(status)
                .message().type(MessageType.JSON)
                .body(new ClassPathResource(expectedResource)));
    }

    protected void validateResponse(TestCaseRunner runner,
                                    HttpClient URL,
                                    HttpStatus status,
                                    Object expectedPayload) {
        runner.$(http().client(URL)
                .receive()
                .response(status)
                .message().type(MessageType.JSON)
                .body(new ObjectMappingPayloadBuilder(expectedPayload, new ObjectMapper())));
    }
}
