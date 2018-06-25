package due.debugchain.chain;

import lombok.Data;
import org.web3j.abi.datatypes.Address;
import org.web3j.tuples.generated.Tuple11;

import java.math.BigInteger;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Data class representing an issue struct from Ethereum contract.
 */
@Data
public class IssueStruct {
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


    /**
     * Parses tuple to issue.
     *
     * @param tuple tuple e.g. from contract invocation
     * @return parsed issue
     */
    // TODO: move into mapper
    public static IssueStruct fromTuple(
        Tuple11<BigInteger, BigInteger, String, List<String>, List<Boolean>, Boolean, Boolean, Boolean,
            Boolean, List<String>, List<BigInteger>> tuple) {
        IssueStruct issue = new IssueStruct();
        issue.setId(tuple.getValue1().longValue());
        issue.setDonationSum(tuple.getValue2());
        issue.setDeveloper(new Address(tuple.getValue3()));
        issue.setReviewers(tuple.getValue4().stream().map(Address::new).collect(toList()));
        issue.setReviewStatus(tuple.getValue5());
        issue.setApproved(tuple.getValue6());
        issue.setLocked(tuple.getValue7());
        issue.setDeveloped(tuple.getValue8());
        issue.setCompleted(tuple.getValue9());
        issue.setDonators(tuple.getValue10().stream().map(Address::new).collect(toList()));
        issue.setDonationValues(tuple.getValue11());
        return issue;
    }
}
