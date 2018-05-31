package due.debugchain.persistence.entities;

import lombok.Data;
import org.web3j.abi.datatypes.Address;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Entity class for persistence of reviewers (mainly their address).
 */
@Entity
@Data
public class ReviewerEntity {

    @Id
    private Long gitlabId;

    private Address address;
}
