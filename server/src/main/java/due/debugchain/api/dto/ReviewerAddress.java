package due.debugchain.api.dto;

import lombok.Data;
import org.web3j.abi.datatypes.Address;

import javax.validation.constraints.NotNull;

/**
 * DTO class for persisting an address for a reviewer.
 */
@Data
public class ReviewerAddress {
    @NotNull
    private Address address;
}
