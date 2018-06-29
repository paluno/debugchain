package due.debugchain.api.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * API data class representing a persisted DebugChain user.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserResource extends AddressResource {
    private Long gitlabId;
    private Long pendingWithdrawals;
}
