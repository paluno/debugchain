package due.debugchain.api.dto;

import lombok.Data;
import org.web3j.abi.datatypes.Address;

import java.math.BigInteger;
import java.util.List;

@Data
public class IssueResource {

    private Long id;
    private BigInteger donationSum;
    private Address developer;
    private List<Address> reviewers;
    private List<Boolean> reviewStatus;
    private boolean approved;
    private boolean locked;
    private boolean developed;
    private boolean completed;
    private List<Address> donators;
    private List<BigInteger> donationValues;
}
