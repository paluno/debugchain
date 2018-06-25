package due.debugchain.chain;

import due.debugchain.CacheInvalidationRule;
import due.debugchain.IntegrationTest;
import due.debugchain.contracts.DebugChain;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.tuples.generated.Tuple11;

import java.math.BigInteger;
import java.util.List;

import static java.math.BigInteger.ZERO;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class ContractServiceTest extends IntegrationTest {

    private String address = "0xMOCK";

    private DebugChain mockContract = mock(DebugChain.class);

    @Rule
    @Autowired
    public CacheInvalidationRule cacheInvalidationRule;

    @Autowired
    private ContractService service;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @MockBean
    private ContractProvider contractProvider;

    @Before
    public void setup() {
        // provide mocked contract instance
        doReturn(mockContract).when(contractProvider).readOnlyContract(address);
    }

    @Test
    public void shouldCacheIssue() {
        long issueId = 1L;
        BigInteger bigIssueId = BigInteger.valueOf(issueId);
        doReturn(issueCall(bigIssueId)).when(mockContract).getIssue(bigIssueId);
        IssueStruct issueFirst = service.getIssue(address, issueId);
        IssueStruct issueSecond = service.getIssue(address, issueId);
        // make sure only called once for real
        verify(mockContract, times(1)).getIssue(bigIssueId);
        assertThat(issueFirst).satisfies(issue -> {
            assertThat(issue.getId()).isEqualTo(issueId);
            assertThat(issue.getId()).isEqualTo(issueSecond.getId());
        });
    }

    @Test
    public void shouldCacheIssuesSeparately() {
        long issueIdOne = 1L;
        long issueIdTwo = 2L;
        BigInteger bigIssueIdOne = BigInteger.valueOf(issueIdOne);
        BigInteger bigIssueIdTwo = BigInteger.valueOf(issueIdTwo);
        doReturn(issueCall(bigIssueIdOne)).when(mockContract).getIssue(bigIssueIdOne);
        doReturn(issueCall(bigIssueIdTwo)).when(mockContract).getIssue(bigIssueIdTwo);
        IssueStruct issueOne = service.getIssue(address, issueIdOne);
        IssueStruct issueTwo = service.getIssue(address, issueIdTwo);
        assertThat(issueOne.getId()).isEqualTo(issueIdOne);
        assertThat(issueTwo.getId()).isEqualTo(issueIdTwo);
        verify(mockContract, times(1)).getIssue(bigIssueIdOne);
        verify(mockContract, times(1)).getIssue(bigIssueIdTwo);
    }

    @Test
    public void shouldEvictIssueCacheOnUpdate() {
        long issueId = 1L;
        BigInteger bigIssueId = BigInteger.valueOf(issueId);
        doReturn(issueCall(bigIssueId)).when(mockContract).getIssue(bigIssueId);
        service.getIssue(address, issueId); // fetch once
        eventPublisher.publishEvent(new IssueUpdateEvent(this, address, issueId));
        service.getIssue(address, issueId); // fetch second time
        verify(mockContract, times(2)).getIssue(bigIssueId);
    }

    private RemoteCall<Tuple11<BigInteger, BigInteger, String, List<String>, List<Boolean>, Boolean, Boolean, Boolean,
        Boolean, List<String>, List<BigInteger>>> issueCall(BigInteger issueId) {
        return new RemoteCall<>(() -> new Tuple11<>(issueId, ZERO, "0x4ad3c9d222043e219901353bf1a0cdbef4570c68", emptyList(), emptyList(), false, false, false,
            false, emptyList(), emptyList())
        );
    }
}