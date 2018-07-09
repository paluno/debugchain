package due.debugchain.chain;

import due.debugchain.contracts.DebugChain;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.datatypes.Event;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.tx.ReadonlyTransactionManager;
import org.web3j.tx.TransactionManager;

import java.math.BigInteger;
import java.util.function.Consumer;

import static due.debugchain.contracts.DebugChain.*;
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
     * @param eventPublisher app event publisher
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
        Consumer<BigInteger> emitter = id -> emitIssueUpdate(contractAddress, id.longValue());
        // TODO try to generify (rx.merge won't work with different types)
        contract.issueDeletedEventObservable(filter(ISSUEDELETED_EVENT, contractAddress)).subscribe(event -> emitter.accept(event._id));
        contract.issueApprovedEventObservable(filter(ISSUEAPPROVED_EVENT, contractAddress)).subscribe(event -> emitter.accept(event._id));
        contract.issueCompletedEventObservable(filter(ISSUECOMPLETED_EVENT, contractAddress)).subscribe(event -> emitter.accept(event._id));
        contract.issueLockedEventObservable(filter(ISSUELOCKED_EVENT, contractAddress)).subscribe(event -> emitter.accept(event._id));
        contract.issueUnlockedEventObservable(filter(ISSUEUNLOCKED_EVENT, contractAddress)).subscribe(event -> emitter.accept(event._id));
        contract.issueResetEventObservable(filter(ISSUERESET_EVENT, contractAddress)).subscribe(event -> emitter.accept(event._id));
        return contract;
    }

    private void emitIssueUpdate(String contractAddress, Long issueId) {
        eventPublisher.publishEvent(new IssueUpdateEvent(this, contractAddress, issueId));
    }

    private static EthFilter filter(Event event, String address) {
        EthFilter filter = new EthFilter(LATEST, LATEST, address.substring(2)); // works only with '0x' cut off
        filter.addSingleTopic(EventEncoder.encode(event));
        return filter;
    }
}
