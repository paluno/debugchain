package due.debugchain.api.mappers;

import due.debugchain.api.dto.MembershipResource;
import due.debugchain.persistence.entities.MembershipEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;

/**
 * Mapper specification for converting membership objects.
 */
@Mapper
public interface MembershipMapper {
    /**
     * Specification for collection converter from persistence entity to resource.
     *
     * @param entities persistence entities
     * @return mapped membership resources
     */
    Collection<MembershipResource> entitiesToResources(Collection<MembershipEntity> entities);

    /**
     * Specification for collection converter from resource to persistence entity .
     *
     * @param resources membership resources
     * @return mapped persistence entities
     */
    Collection<MembershipEntity> resourcesToEntities(Collection<MembershipResource> resources);

    /**
     * Specification for converter from persistence entity to resource.
     *
     * @param entity persistence entity
     * @return mapped membership resource
     */
    @Mapping(source = "identity.projectGitlabId", target = "projectGitlabId")
    @Mapping(source = "identity.userGitlabId", target = "userGitlabId")
    MembershipResource entityToResource(MembershipEntity entity);

    /**
     * Specification for converter from resource to persistence entity.
     *
     * @param resource membership resource
     * @return mapped persistence entity
     */
    @Mapping(source = "projectGitlabId", target = "identity.projectGitlabId")
    @Mapping(source = "userGitlabId", target = "identity.userGitlabId")
    MembershipEntity resourceToEntity(MembershipResource resource);
}
