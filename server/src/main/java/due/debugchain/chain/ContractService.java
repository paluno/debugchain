package due.debugchain.chain;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.tuples.generated.Tuple4;

import java.math.BigInteger;
import java.util.List;

/**
 * Service for contract interaction.
 */
@Service
@RequiredArgsConstructor
public class ContractService {

    private final ContractProvider provider;

    // TODO: return domain object instead of tuple?
    @Cacheable(value = "issuesCache", key = "#issueId")
    public Tuple4<BigInteger, BigInteger, List<String>, Boolean> getIssue(String address, Long issueId) {
        return send(provider.readOnlyContract(address).getIssue(BigInteger.valueOf(issueId)));
    }

    // helper method for wrapping checked exceptions from RemoteCall#send
    private static <T> T send(RemoteCall<T> call) {
        try {
            return call.send();
        } catch (Exception e) {
            throw new ContractException(e);
        }
    }
}
