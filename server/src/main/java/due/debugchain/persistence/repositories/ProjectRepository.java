package due.debugchain.persistence.repositories;

import due.debugchain.persistence.entities.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Database repository for project persistence.
 */
@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
}
