package due.debugchain.api.dto;

import lombok.Data;
import org.web3j.abi.datatypes.Address;

/**
 * DTO class for persisting a project member.
 */
@Data
public class MembershipRequest {
    private Address address;
    private boolean reviewer;

//    public void setAddress(String address) TODO: allow only valid addresses
}
