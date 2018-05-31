package due.debugchain;

import due.debugchain.contracts.HelloWorld;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tx.Contract;
import org.web3j.tx.ManagedTransaction;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest
public class IntegrationTest {

    @Autowired
    protected Web3j web3j;

    @Autowired
    protected WebApplicationContext context;

    protected MockMvc mockMvc;

    @Before
    public void mvcSetup() {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .build();
    }

    @Test
    public void testDeploy() throws Exception {
        Credentials credentials = Credentials.create("2631b689696e69aae84657d1d1f1bebae62bd50af72dfff2ef3050c9828bf8f3");
        HelloWorld contract = HelloWorld
            .deploy(web3j, credentials, ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT)
            .send();
        assertThat(contract.renderHelloWorld().send()).isEqualTo("Hello World!");
    }
}
