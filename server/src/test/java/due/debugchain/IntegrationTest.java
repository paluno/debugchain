package due.debugchain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.web3j.protocol.Web3j;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest
public abstract class IntegrationTest {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    protected Web3j web3j;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    protected MockMvc mockMvc;

    private MockRestServiceServer server;

    @Before
    public void mvcSetup() {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(springSecurity())
            .build();
        server = MockRestServiceServer.createServer(restTemplate);
    }

    protected RequestPostProcessor userToken() {
        String tokenHeader = "Bearer 9a3a65322e7283741f2710d5d4e796af2b0bc46131a9e21da4a8761b96761043";
        server.expect(requestTo("http://localhost/api/v4/user"))
            .andExpect(method(HttpMethod.GET))
            .andExpect(header(HttpHeaders.AUTHORIZATION, equalTo(tokenHeader)))
            .andRespond(withSuccess(userResponse(), MediaType.APPLICATION_JSON));
        return mockRequest -> {
            mockRequest.addHeader("Authorization", tokenHeader);
            return mockRequest;
        };
    }

    private String userResponse() {
        GitLabUser user = new GitLabUser();
        user.setAdmin(false);
        user.setEmail("user@example.com");
        user.setId(2L);
        user.setName("Benutzer");
        user.setUsername("user");
        user.setState("active");
        try {
            return objectMapper.writeValueAsString(user);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
