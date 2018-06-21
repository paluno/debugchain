package due.debugchain.chain;

import due.debugchain.contracts.DebugChain;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.tx.ReadonlyTransactionManager;
import org.web3j.tx.TransactionManager;

import static org.web3j.protocol.core.DefaultBlockParameterName.EARLIEST;
import static org.web3j.protocol.core.DefaultBlockParameterName.LATEST;
import static org.web3j.tx.Contract.GAS_LIMIT;
import static org.web3j.tx.ManagedTransaction.GAS_PRICE;

/**
 * Provider for loading contracts.
 */
@Component
@RequiredArgsConstructor
public class ContractProvider {

    private final Web3j web3j;

    private final TransactionManager transactionManager;

    private final ApplicationEventPublisher eventPublisher;

    /**
     * Default constructor for injecting web3j.
     * Will setup read-only transaction manager.
     *
     * @param web3j web3j instance
     */
    @Autowired
    public ContractProvider(Web3j web3j, ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
        this.web3j = web3j;
        this.transactionManager = new ReadonlyTransactionManager(web3j, "");
    }

    /**
     * Loads contract in read-only mode.
     *
     * @param contractAddress address of contract to load
     * @return read-only contract
     */
    public DebugChain readOnlyContract(String contractAddress) {
        DebugChain contract = DebugChain.load(contractAddress, web3j, transactionManager, GAS_PRICE, GAS_LIMIT);
        // TODO subscribe to more events
        contract.issueDeletedEventObservable(EARLIEST, LATEST).subscribe(event -> emitIssueUpdate(contractAddress, event._id.longValue()));
        return contract;
    }

    private void emitIssueUpdate(String contractAddress, Long issueId) {
        eventPublisher.publishEvent(new IssueUpdateEvent(this, contractAddress, issueId));
    }
}
