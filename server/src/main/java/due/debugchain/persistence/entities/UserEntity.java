package due.debugchain.persistence.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.web3j.abi.datatypes.Address;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Collection;

/**
 * Entity class for persistence of reviewers (mainly their address).
 */
@Entity
@Data
@NoArgsConstructor
public class UserEntity {

    /**
     * Constructor for initializing entity with corresponding GitLab ID.
     *
     * @param gitlabId user's GitLab ID
     */
    public UserEntity(Long gitlabId) {
        this.gitlabId = gitlabId;
    }

    @Id
    private Long gitlabId;

    private Address address;

    @OneToMany(mappedBy = "identity.userGitlabId", cascade = CascadeType.ALL)
    @JsonBackReference
    private Collection<MembershipEntity> memberships;

}
