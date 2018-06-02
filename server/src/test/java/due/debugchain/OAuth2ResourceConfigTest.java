package due.debugchain;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OAuth2ResourceConfigTest extends IntegrationTest {

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer server;

    @Before
    public void setup() {
        server = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void denyApiWithoutToken() throws Exception {
        mockMvc.perform(get("/blocks"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    public void allowApiWithToken() throws Exception {
        String token = "Bearer mock-token";
        server.expect(requestTo("http://localhost/api/v4/user"))
            .andExpect(method(HttpMethod.GET))
            .andExpect(header(HttpHeaders.AUTHORIZATION, equalTo(token)))
            .andRespond(withSuccess("{\n" +
                "  \"id\": 1,\n" +
                "  \"name\": \"Administrator\",\n" +
                "  \"username\": \"root\",\n" +
                "  \"state\": \"active\",\n" +
                "  \"avatar_url\": \"https://www.gravatar.com/avatar/e64c7d89f26bd1972efa854d13d7dd61?s=80\\u0026d=identicon\",\n" +
                "  \"web_url\": \"http://gitlab.example.com/root\",\n" +
                "  \"created_at\": \"2018-06-02T11:00:25.606Z\",\n" +
                "  \"bio\": null,\n" +
                "  \"location\": null,\n" +
                "  \"skype\": \"\",\n" +
                "  \"linkedin\": \"\",\n" +
                "  \"twitter\": \"\",\n" +
                "  \"website_url\": \"\",\n" +
                "  \"organization\": null,\n" +
                "  \"last_sign_in_at\": \"2018-06-02T11:03:22.917Z\",\n" +
                "  \"confirmed_at\": \"2018-06-02T11:00:25.339Z\",\n" +
                "  \"last_activity_on\": null,\n" +
                "  \"email\": \"admin@example.com\",\n" +
                "  \"theme_id\": 1,\n" +
                "  \"color_scheme_id\": 1,\n" +
                "  \"projects_limit\": 100000,\n" +
                "  \"current_sign_in_at\": \"2018-06-02T11:03:22.917Z\",\n" +
                "  \"identities\": [],\n" +
                "  \"can_create_group\": true,\n" +
                "  \"can_create_project\": true,\n" +
                "  \"two_factor_enabled\": false,\n" +
                "  \"external\": false,\n" +
                "  \"is_admin\": true\n" +
                "}", MediaType.APPLICATION_JSON));
        mockMvc.perform(get("/blocks")
            .header(HttpHeaders.AUTHORIZATION, token))
            .andExpect(status().isOk());
    }
}