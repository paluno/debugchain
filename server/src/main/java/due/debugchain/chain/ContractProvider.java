package due.debugchain.chain;

import due.debugchain.contracts.DebugChain;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.tx.ReadonlyTransactionManager;
import org.web3j.tx.TransactionManager;

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

    /**
     * Default constructor for injecting web3j.
     * Will setup read-only transaction manager.
     *
     * @param web3j web3j instance
     */
    @Autowired
    public ContractProvider(Web3j web3j) {
        this.web3j = web3j;
        this.transactionManager = new ReadonlyTransactionManager(web3j, "");
    }

    /**
     * Loads contract in read-only mode.
     *
     * @param address address of contract to load
     * @return read-only contract
     */
    public DebugChain readOnlyContract(String address) {
        return DebugChain.load(address, web3j, transactionManager, GAS_PRICE, GAS_LIMIT);
    }
}
