package due.debugchain.persistence.repositories;

import due.debugchain.persistence.entities.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
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

    @Query(value = "SELECT u.* FROM user_entity u, membership_entity m WHERE m.user_gitlab_id = u.gitlab_id AND m.project_gitlab_id = ?1  AND m.reviewer = true"
            , nativeQuery = true)
    Collection<UserEntity> findUserByProjectGitlabId(@Param("projectId") Long projectGitlabId);
}
