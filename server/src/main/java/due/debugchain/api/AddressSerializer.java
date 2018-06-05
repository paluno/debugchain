package due.debugchain.api;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import org.web3j.abi.datatypes.Address;

import java.io.IOException;

/**
 * Custom JSON serializer for converting an ethereum address to a JSON string value.
 */
@JsonComponent // TODO: not picked up?
public class AddressSerializer extends JsonSerializer<Address> {

    @Override
    public void serialize(Address address, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(address.toString());
    }
}
