package due.debugchain.persistence;

import due.debugchain.auth.GitLabUser;
import due.debugchain.persistence.entities.MembershipEntity;
import due.debugchain.persistence.entities.UserEntity;
import due.debugchain.persistence.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

/**
 * Service for business operations on users.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * Retrieves a user from persistence.
     *
     * @param gitlabId user's GitLab ID
     * @return user or empty when not found
     */
    public Optional<UserEntity> getUser(Long gitlabId) {
        return userRepository.findById(gitlabId);
    }

    /**
     * Persists a new user.
     *
     * @param userEntity user to persist
     * @return persisted user
     * @throws RuntimeException if user with same ID already exists
     */
    public UserEntity createUser(UserEntity userEntity) {
        if (userRepository.existsById(userEntity.getGitlabId())) {
            throw new RuntimeException("User already exists");
        }
        return userRepository.save(userEntity);
    }

    /**
     * Resolves user associated current authentication.
     *
     * @return currently authenticated user-entity
     */
    public Optional<UserEntity> currentUser() {
        GitLabUser principal = (GitLabUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findById(principal.getId());
    }

    public Collection<MembershipEntity> saveMemberships(UserEntity userEntity, Collection<MembershipEntity> memberships) {
        userEntity.setMemberships(memberships);
        return userRepository.save(userEntity).getMemberships();
    }

    public UserEntity updateUser(UserEntity user) {
        return userRepository.save(user);
    }

}
