package due.debugchain.chain;

import due.debugchain.IntegrationTest;
import due.debugchain.contracts.DebugChain;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tx.Contract;
import org.web3j.tx.ManagedTransaction;

import java.math.BigInteger;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class IssuesControllerTest extends IntegrationTest {

    @Autowired
    protected Web3j web3j;

    private Credentials credentials = Credentials.create("c87509a1c067bbde78beb793e6fa76530b6382a4c0241e5e4a9ec0a0f44dc0d3");
    //private Credentials credentials = Credentials.create("668740748f79c03B1798767DC803deEb169bfDCD");

    private BigInteger pId = new BigInteger("1");

    private DebugChain contract;

    @Before
    public void setup() throws Exception {

        deployContract();
    }

    public void deployContract() throws Exception {

        this.contract = DebugChain
                .deploy(this.web3j, this.credentials, ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT, this.pId)
                .send();

    }

    @Test
    public void addIssues() throws Exception{

        contract.donate(new BigInteger("1"), new BigInteger("1")).send();
        contract.donate(new BigInteger("2"), new BigInteger("1")).send();

        List issues = contract.getIssueLookup().send();

        assertThat(issues.size()).isEqualTo(2);
    }

    @Test
    public void getIssuesWithId() throws Exception {
        mockMvc.perform(get("/api/projects/1/issues/1")
                .with(userToken()))
                .andExpect(status().isOk());

    }

    @Test
    public void getIssueList() throws Exception {
        mockMvc.perform(get("/api/projects/1/issues")
                .with(userToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", iterableWithSize(2)));
    }



}
