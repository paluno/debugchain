package due.debugchain.chain;

import due.debugchain.IntegrationTest;
import due.debugchain.contracts.DebugChain;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;

import static due.debugchain.chain.IssueStruct.Status.APPROVED;
import static due.debugchain.chain.IssueStruct.Status.DEFAULT;
import static java.math.BigInteger.valueOf;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.web3j.utils.Convert.Unit.ETHER;
import static org.web3j.utils.Convert.toWei;

public class ContractIntegrationTest extends IntegrationTest {

    @Autowired
    private ContractService service;

    @Test // TODO: this works only when cache is disabled
    public void evictCacheOnIssueUpdate() throws Exception {
        DebugChain contract = deployContract();
        Long issueId = 1L;
        BigInteger bigIssueId = valueOf(issueId);
        contract.donate(bigIssueId, toWei("1", ETHER).toBigInteger()).send();
        IssueStruct issue = service.getIssue(contract.getContractAddress(), issueId);
        assertThat(issue.getLifecycleStatus()).isEqualTo(DEFAULT);
        contract.setApproved(bigIssueId, true, singletonList(credentials.getAddress())).send();
        issue = service.getIssue(contract.getContractAddress(), issueId);
        assertThat(issue.getLifecycleStatus()).isEqualTo(APPROVED);
    }
}
