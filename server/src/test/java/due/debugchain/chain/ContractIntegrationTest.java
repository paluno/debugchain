package due.debugchain.chain;

import due.debugchain.IntegrationTest;
import due.debugchain.contracts.DebugChain;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigInteger;

import static due.debugchain.chain.IssueStruct.Status.APPROVED;
import static due.debugchain.chain.IssueStruct.Status.DEFAULT;
import static java.lang.Thread.sleep;
import static java.math.BigInteger.valueOf;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.web3j.utils.Convert.Unit.ETHER;
import static org.web3j.utils.Convert.toWei;

public class ContractIntegrationTest extends IntegrationTest {

    @Value("${web3j.pollingInterval}")
    private Long pollingInterval;

    @Autowired
    private ContractService service;

    @Test
    public void evictCacheOnIssueUpdate() throws Exception {
        DebugChain contract = deployContract();
        Long issueId = 1L;
        BigInteger bigIssueId = valueOf(issueId);
        contract.donate(bigIssueId, toWei("1", ETHER).toBigInteger()).send();
        IssueStruct issue = service.getIssue(contract.getContractAddress(), issueId);
        assertThat(issue.getLifecycleStatus()).isEqualTo(DEFAULT);
        contract.setApproved(bigIssueId, true, singletonList(credentials.getAddress())).send();
        sleep(pollingInterval + 500); // wait for events to be fired
        issue = service.getIssue(contract.getContractAddress(), issueId);
        assertThat(issue.getLifecycleStatus()).isEqualTo(APPROVED);
    }
}
