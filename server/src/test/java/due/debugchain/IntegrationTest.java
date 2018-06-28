package due.debugchain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import due.debugchain.auth.GitLabUser;
import due.debugchain.contracts.DebugChain;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;

import java.math.BigInteger;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.client.ExpectedCount.manyTimes;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.web3j.tx.Contract.GAS_LIMIT;
import static org.web3j.tx.ManagedTransaction.GAS_PRICE;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest
@Sql( {"/schema.sql", "/test_data.sql"})
public abstract class IntegrationTest {

    protected static Long USER_ID = 676L;

    // private key from ganache-cli using mnemonic:
    // 'candy maple cake sugar pudding cream honey rich smooth crumble sweet treat'
    private static final String PRIVATE_KEY = "c87509a1c067bbde78beb793e6fa76530b6382a4c0241e5e4a9ec0a0f44dc0d3";

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
            .alwaysDo(result -> server.reset()) // reset mocking of user-requests to GitLab
            .apply(springSecurity())
            .build();
        server = MockRestServiceServer.createServer(restTemplate);
    }

    protected RequestPostProcessor userToken() {
        return userToken(USER_ID);
    }

    protected RequestPostProcessor userToken(Long userId) {
        String tokenHeader = "Bearer 9a3a65322e7283741f2710d5d4e796af2b0bc46131a9e21da4a8761b96761043";
        server.expect(manyTimes(), requestTo("http://localhost/api/v4/user"))
            .andExpect(method(HttpMethod.GET))
            .andExpect(header(HttpHeaders.AUTHORIZATION, equalTo(tokenHeader)))
            .andRespond(withSuccess(userResponse(userId), MediaType.APPLICATION_JSON));
        return mockRequest -> {
            mockRequest.addHeader("Authorization", tokenHeader);
            return mockRequest;
        };
    }

    private String userResponse(Long userId) {
        GitLabUser user = new GitLabUser();
        user.setAdmin(false);
        user.setEmail("user@example.com");
        user.setId(userId);
        user.setName("Benutzer");
        user.setUsername("user");
        user.setState("active");
        try {
            return objectMapper.writeValueAsString(user);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    protected DebugChain deployContract() throws Exception {
        return deployContract(999L);
    }

    // helper method for setting up contract for integration testing
    protected DebugChain deployContract(long projectId) throws Exception {
        Credentials credentials = Credentials.create(PRIVATE_KEY);
        // deploy contract
        return DebugChain
            .deploy(web3j, credentials, GAS_PRICE, GAS_LIMIT, BigInteger.valueOf(projectId))
            .send();
    }


}
