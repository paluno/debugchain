package due.debugchain.api.mappers;

import due.debugchain.api.dto.ProjectResource;
import due.debugchain.persistence.entities.ProjectEntity;
import org.mapstruct.Mapper;

/**
 * Mapper specification for converting project-related objects.
 */
@Mapper
public interface ProjectMapper {
    /**
     * Maps a ProjectEntity to a ProjectResource
     *
     * @param entity persistence entity
     * @return mapped project resource
     */
    ProjectResource entityToResource(ProjectEntity entity);

    /**
     * Maps a ProjectEntity to a ProjectResource
     *
     * @param resource project resource
     * @return mapped persistence entity
     */
    ProjectEntity resourceToEntity(ProjectResource resource);
}
