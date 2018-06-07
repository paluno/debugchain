package due.debugchain.persistence.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Collection;

/**
 * Entity class for persistence of projects.
 */
@Entity
@Data
public class ProjectEntity {
    @Id
    private Long gitlabId;

    private String address;

    @OneToMany(mappedBy = "identity.projectGitlabId", cascade = CascadeType.ALL)
    @JsonBackReference
    private Collection<MembershipEntity> memberships;
}
