package due.debugchain.persistence;

import org.web3j.abi.datatypes.Address;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Persistence converter for converting between {@link Address} and {@link String} for JPA.
 */
@Converter(autoApply = true)
public class AddressConverter implements AttributeConverter<Address, String> {
    @Override
    public String convertToDatabaseColumn(Address address) {
        if (address == null) {
            return null;
        }
        return address.toString();
    }

    @Override
    public Address convertToEntityAttribute(String dbData) {
        return new Address(dbData);
    }
}
