package due.debugchain.api.dto;

import lombok.Data;

/**
 * API data class representing a persisted DebugChain project.
 */
@Data
public class ProjectResource {
    private Long gitlabId;
    private String address;
}
