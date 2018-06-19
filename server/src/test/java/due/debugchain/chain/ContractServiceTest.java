package due.debugchain.chain;

import due.debugchain.IntegrationTest;
import due.debugchain.contracts.DebugChain;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
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

    @Autowired
    private ContractService service;

    @MockBean
    private ContractProvider contractProvider;

    @Before
    public void setup() {
        // provide mocked contract instance
        doReturn(mockContract).when(contractProvider).readOnlyContract(address);
    }

    @Test
    public void testIssueCache() {
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
}