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
import org.web3j.tuples.generated.Tuple4;

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
        RemoteCall<Tuple4<BigInteger, BigInteger, List<String>, Boolean>> issueCall =
            new RemoteCall<>(() -> new Tuple4<>(bigIssueId, ZERO, emptyList(), false));
        doReturn(issueCall).when(mockContract).getIssue(bigIssueId);
        Tuple4<BigInteger, BigInteger, List<String>, Boolean> issueFirst = service.getIssue(address, issueId);
        Tuple4<BigInteger, BigInteger, List<String>, Boolean> issueSecond = service.getIssue(address, issueId);
        // make sure only called once for real
        verify(mockContract, times(1)).getIssue(bigIssueId);
        assertThat(issueFirst).satisfies(issue -> {
            assertThat(issue.getValue1()).isEqualTo(issueId);
            assertThat(issue.getValue1()).isEqualTo(issueSecond.getValue1());
            assertThat(issue.getValue2()).isEqualTo(issueSecond.getValue2());
            assertThat(issue.getValue3().size()).isEqualTo(issueSecond.getValue3().size());
            assertThat(issue.getValue4()).isEqualTo(issueSecond.getValue4());
        });
    }

    @Test
    public void shouldCacheIssuesSeparately() {
        long issueIdOne = 1L;
        long issueIdTwo = 2L;
        BigInteger bigIssueIdOne = BigInteger.valueOf(issueIdOne);
        BigInteger bigIssueIdTwo = BigInteger.valueOf(issueIdTwo);
        RemoteCall<Tuple4<BigInteger, BigInteger, List<String>, Boolean>> issueCallOne =
            new RemoteCall<>(() -> new Tuple4<>(bigIssueIdOne, ZERO, emptyList(), false));
        RemoteCall<Tuple4<BigInteger, BigInteger, List<String>, Boolean>> issueCallTwo =
            new RemoteCall<>(() -> new Tuple4<>(bigIssueIdTwo, ZERO, emptyList(), false));
        doReturn(issueCallOne).when(mockContract).getIssue(bigIssueIdOne);
        doReturn(issueCallTwo).when(mockContract).getIssue(bigIssueIdTwo);
        Tuple4<BigInteger, BigInteger, List<String>, Boolean> issueOne = service.getIssue(address, issueIdOne);
        Tuple4<BigInteger, BigInteger, List<String>, Boolean> issueTwo = service.getIssue(address, issueIdTwo);
        assertThat(issueOne.getValue1()).isEqualTo(issueIdOne);
        assertThat(issueTwo.getValue1()).isEqualTo(issueIdTwo);
        verify(mockContract, times(1)).getIssue(bigIssueIdOne);
        verify(mockContract, times(1)).getIssue(bigIssueIdTwo);
    }

    @Test
    public void shouldEvictIssueCacheOnUpdate() {
        long issueId = 1L;
        BigInteger bigIssueId = BigInteger.valueOf(issueId);
        RemoteCall<Tuple4<BigInteger, BigInteger, List<String>, Boolean>> issueCall =
            new RemoteCall<>(() -> new Tuple4<>(bigIssueId, ZERO, emptyList(), false));
        doReturn(issueCall).when(mockContract).getIssue(bigIssueId);
        service.getIssue(address, issueId); // fetch once
        eventPublisher.publishEvent(new IssueUpdateEvent(this, address, issueId));
        service.getIssue(address, issueId); // fetch second time
        verify(mockContract, times(2)).getIssue(bigIssueId);
    }
}