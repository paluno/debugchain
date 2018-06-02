package due.debugchain;

import due.debugchain.contracts.HelloWorld;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
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
    private Web3j web3j;

    @Test
    public void testDeploy() throws Exception {
        Credentials credentials = Credentials.create("c87509a1c067bbde78beb793e6fa76530b6382a4c0241e5e4a9ec0a0f44dc0d3");
        HelloWorld contract = HelloWorld
            .deploy(web3j, credentials, ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT)
            .send();
        assertThat(contract.renderHelloWorld().send()).isEqualTo("Hello World!");
    }
}
