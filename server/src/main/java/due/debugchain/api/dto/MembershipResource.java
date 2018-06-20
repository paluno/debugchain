package due.debugchain.api.dto;

import lombok.Data;

/**
 * DTO class for persisting a project member.
 */
@Data
public class MembershipResource {
    private Long projectGitlabId;
    private Long userGitlabId;
    private boolean reviewer;
}
