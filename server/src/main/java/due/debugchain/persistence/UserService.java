package due.debugchain.persistence;

import due.debugchain.persistence.entities.UserEntity;
import due.debugchain.persistence.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
     */
    public UserEntity createUser(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }
}
