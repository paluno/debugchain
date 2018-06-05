package due.debugchain.persistence.repositories;

import due.debugchain.persistence.entities.ProjectEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Database repository for project persistence.
 */
@Repository
public interface ProjectRepository extends CrudRepository<ProjectEntity, Long> {
}
