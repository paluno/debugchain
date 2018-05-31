package due.debugchain.persistence.entities;

import lombok.Data;

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

    @OneToMany
    private Collection<ReviewerEntity> reviewers;
}
