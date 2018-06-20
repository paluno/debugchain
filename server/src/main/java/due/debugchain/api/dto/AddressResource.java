package due.debugchain.api.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Wrapper DTO for passing an Ethereum address.
 */
@Data
public class AddressResource {
    @NotNull
    private String address;
}
