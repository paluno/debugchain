package due.debugchain.api.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * API data class representing a persisted DebugChain user.
 */
@Data
public class UserResource {
    private Long gitlabId;
    @NotNull
    private String address;
}
