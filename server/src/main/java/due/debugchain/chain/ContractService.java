package due.debugchain.chain;

import due.debugchain.contracts.DebugChain;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.RemoteCall;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static due.debugchain.chain.IssueStruct.fromTuple;
import static java.math.BigInteger.valueOf;

/**
 * Service for contract interaction.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ContractService {

    private final ContractProvider provider;

    @Cacheable(value = "issues", key = "#contractAddress + '-' + #issueId")
    public IssueStruct getIssue(String contractAddress, Long issueId) {
        return fromTuple(send(contract(contractAddress).getIssue(valueOf(issueId))));
    }

    @Cacheable(value = "issuesIdList", key = "#contractAddress")
    public List<BigInteger> getIssueIdList(String contractAddress) {
        return send(contract(contractAddress).getIssueLookup());
    }

    @Cacheable(value = "issuesList", key = "#contractAddress")
    public List<IssueStruct> getIssueList(String contractAddress) {

        ArrayList issues = new ArrayList<IssueStruct>();

        getIssueIdList(contractAddress).stream()
                .forEach(item -> issues.add(getIssue(contractAddress, item.longValue())));

        return issues;
    }

    /**
     * Listens for issue updates and evicts cache accordingly.
     * <br>
     * <b>NOTE:</b> this method should probably not be called explicitly
     *
     * @param event event indicating issue update
     */
    @EventListener
    @Caching(evict = {
        @CacheEvict(value = "issues", key = "#event.contractAddress + '-' + #event.issueId"),
        @CacheEvict(value = "issuesList", key = "#event.contractAddress"),
        @CacheEvict(value = "issuesIdList", key = "#event.contractAddress")
    })
    public void evictIssue(IssueUpdateEvent event) {
        log.info(String.format("Cache evicted for issue %s in contract %s", event.getIssueId(), event.getContractAddress()));
        // NOOP
    }

    private DebugChain contract(String address) {
        return provider.readOnlyContract(address);
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
