package due.debugchain.api.mappers;

import due.debugchain.api.dto.UserResource;
import due.debugchain.persistence.entities.UserEntity;
import org.mapstruct.Mapper;
import org.web3j.abi.datatypes.Address;

import java.util.Collection;

/**
 * Mapper specification for converting user-related objects.
 */
@Mapper
public interface UserMapper {
    /**
     * Specification for converter from persistence entity to resource.
     *
     * @param entity persistence entity
     * @return mapped user resource
     */
    UserResource entityToResource(UserEntity entity);

    /**
     * Specification for collection converter from persistence entity to resource.
     *
     * @param entities persistence entities
     * @return mapped user resources
     */
    Collection<UserResource> entitiesToResources(Collection<UserEntity> entities);

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
        if (address.equals(Address.DEFAULT)){
            return null;
        }
        return address.toString();
    }
}
