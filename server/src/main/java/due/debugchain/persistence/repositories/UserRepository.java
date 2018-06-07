package due.debugchain.persistence.repositories;

import due.debugchain.persistence.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * Database repository for reviewer persistence.
 */
@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {
    /**
     * Resolves all users with the specified IDs.
     *
     * @param ids IDs of users to resolve
     * @return resolved users
     */
    Collection<UserEntity> findByGitlabIdIn(Collection<Long> ids);
}
