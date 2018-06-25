package due.debugchain.chain;

/**
 * Exception to be thrown for errors during contract interaction.
 */
public class ContractException extends RuntimeException {
    /**
     * Constructor for passing root cause.
     *
     * @param cause underlying exception
     */
    public ContractException(Throwable cause) {
        super(cause);
    }
}
