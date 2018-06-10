package due.debugchain.api.dto;

import lombok.Data;

/**
 * API data class representing a persisted DebugChain user.
 */
@Data
public class UserResource {
    private Long gitlabId;
    private String address;
}
