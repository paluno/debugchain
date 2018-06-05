package due.debugchain.persistence.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
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
public class UserEntity {

    @Id
    private Long gitlabId;

    private Address address;

    @OneToMany(mappedBy = "identity.userGitlabId", cascade = CascadeType.ALL)
    @JsonBackReference
    private Collection<MembershipEntity> memberships;

}
