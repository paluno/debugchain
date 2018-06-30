package due.debugchain.api.dto;

import lombok.Data;

import java.math.BigInteger;
import java.util.List;

@Data
public class IssueResource {

    private Long id;
    private BigInteger donationSum;
    private String developer;
    private List<String> reviewers;
    private List<Boolean> reviewStatus;
    private boolean approved;
    private boolean locked;
    private boolean developed;
    private boolean completed;
    private List<String> donators;
    private List<BigInteger> donationValues;
}
