package due.debugchain.chain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.web3j.abi.datatypes.Address;
import org.web3j.tuples.generated.Tuple8;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static due.debugchain.chain.IssueStruct.Status.fromCode;
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
    private Status lifecycleStatus;
    private List<Address> donators;
    private List<BigInteger> donationValues;


    /**
     * Parses tuple to issue.
     *
     * @param tuple tuple e.g. from contract invocation
     * @return parsed issue
     */
    public static IssueStruct fromTuple(Tuple8<BigInteger, BigInteger, String, List<String>,
        List<Boolean>, BigInteger, List<String>, List<BigInteger>> tuple) {
        // TODO move into mapper? too many edge cases?
        IssueStruct issue = new IssueStruct();
        issue.setId(tuple.getValue1().longValue());
        issue.setDonationSum(tuple.getValue2());
        issue.setDeveloper(new Address(tuple.getValue3()));
        issue.setReviewers(toAddresses(tuple.getValue4()));
        issue.setReviewStatus(tuple.getValue5());
        issue.setLifecycleStatus(fromCode(tuple.getValue6()).orElseThrow(IllegalArgumentException::new));
        issue.setDonators(toAddresses(tuple.getValue7()));
        issue.setDonationValues(tuple.getValue8());
        return issue;
    }

    /**
     * Enum representing statuses of issues in contract.
     */
    @AllArgsConstructor
    @Getter
    public enum Status {
        /**
         * Initial state of an issue.
         */
        DEFAULT(BigInteger.valueOf(0)),
        /**
         * Issue has been approved by maintainer.
         */
        APPROVED(BigInteger.valueOf(1)),
        /**
         * Issue is locked by developer (therefore being developed).
         */
        LOCKED(BigInteger.valueOf(2)),
        /**
         * Issue has been developed and is being reviewed.
         */
        DEVELOPED(BigInteger.valueOf(3)),
        /**
         * Issue has been accepted during review and is completed.
         */
        COMPLETED(BigInteger.valueOf(4));
        private final BigInteger code;

        /**
         * Resolves Status instance from corresponding status code.
         *
         * @param code status code
         * @return matching status
         */
        static Optional<Status> fromCode(BigInteger code) {
            return Arrays.stream(values())
                .filter(s -> s.getCode().equals(code))
                .findFirst();
        }
    }

    private static List<Address> toAddresses(List<String> addressValues) {
        return addressValues.stream().map(Address::new).collect(toList());
    }
}
