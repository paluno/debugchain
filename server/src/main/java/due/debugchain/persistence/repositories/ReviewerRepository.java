package due.debugchain.persistence.repositories;

import due.debugchain.persistence.entities.ReviewerEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Database repository for reviewer persistence.
 */
@Repository
public interface ReviewerRepository extends CrudRepository<ReviewerEntity, Long> {
}
