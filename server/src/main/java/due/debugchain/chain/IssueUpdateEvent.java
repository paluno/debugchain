package due.debugchain.chain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.context.ApplicationEvent;

/**
 * Event indicating that a specific issue in a specific contract has been updated.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class IssueUpdateEvent extends ApplicationEvent {

    private String contractAddress;

    private Long issueId;

    /**
     * Constructor for passing info about the updated issue.
     *
     * @param source          event emitter
     * @param contractAddress address of contract in which the issue has been updated
     * @param issueId         ID of issue that has been updated
     */
    public IssueUpdateEvent(Object source, String contractAddress, Long issueId) {
        super(source);
        this.contractAddress = contractAddress;
        this.issueId = issueId;
    }
}
