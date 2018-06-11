package due.debugchain.persistence.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.io.Serializable;

/**
 * Model representing membership-connection between users and projects.
 */
@Data
@Entity
@NoArgsConstructor
public class MembershipEntity {

    /**
     * Default constructor for passing defining parameters for membership.
     *
     * @param project  project the user is member of
     * @param user     user whose member of project
     * @param reviewer whether the user is a reviewer for the project
     */
    public MembershipEntity(ProjectEntity project, UserEntity user, boolean reviewer) {
        this.identity = new MembershipIdentity(project.getGitlabId(), user.getGitlabId());
        this.reviewer = reviewer;
    }

    @EmbeddedId
    private MembershipIdentity identity;

    private boolean reviewer;

    /**
     * Composite identity.
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MembershipIdentity implements Serializable {
        private Long projectGitlabId;
        private Long userGitlabId;
    }
}
