package due.debugchain.api.dto;

import due.debugchain.chain.IssueStruct;
import lombok.Data;
import org.web3j.abi.datatypes.Address;

import java.math.BigInteger;
import java.util.List;

@Data
public class IssueResource {

    private Long id;
    private BigInteger donationSum;
    private String developer;
    private List<String> reviewers;
    private List<Boolean> reviewStatus;
    private IssueStruct.Status lifecycleStatus;
    private List<String> donators;
    private List<BigInteger> donationValues;
}
