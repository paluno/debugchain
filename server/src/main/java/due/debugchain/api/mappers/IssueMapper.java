package due.debugchain.api.mappers;

import due.debugchain.api.dto.IssueResource;
import due.debugchain.chain.IssueStruct;
import org.mapstruct.Mapper;
import org.web3j.abi.datatypes.Address;

@Mapper
public interface IssueMapper {

    /**
     * Maps a IssueStruct to a IssueResource
     *
     * @param struct chain entity
     * @return mapped issue resource
     */
    IssueResource entityToResource(IssueStruct struct);

    /**
     * Default implementation for converting a Web3J ethereum address to a string.
     *
     * @param address address to convert
     * @return string representation of address
     */
    default String addressToString(Address address) {
        if (address == null){
            return null;
        }
        return address.toString();
    }
}
