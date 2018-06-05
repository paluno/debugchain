package due.debugchain.persistence;

import due.debugchain.persistence.entities.MembershipEntity;
import due.debugchain.persistence.entities.ProjectEntity;
import due.debugchain.persistence.entities.UserEntity;
import due.debugchain.persistence.repositories.ProjectRepository;
import due.debugchain.persistence.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

/**
 * Service for business logic regarding projects.
 */
@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    private final UserRepository userRepository;

    /**
     * Retrieves a persisted.
     *
     * @param projectId project's GitLab ID
     * @return opt. project
     */
    public Optional<ProjectEntity> getProject(Long projectId) {
        return projectRepository.findById(projectId);
    }

    /**
     * Attaches a user to a project by persiting a new membership connection.
     *
     * @param project  project to attach user to
     * @param user     user to attach to project
     * @param reviewer whether user is reviewer for project
     */
    public void addMember(ProjectEntity project, UserEntity user, boolean reviewer) {
        project.getMemberships().add(new MembershipEntity(project, user, reviewer));
        projectRepository.save(project);
    }

    /**
     * Resolves all users which have a membership in the specified project.
     *
     * @param project project whose members to resolvel
     * @return project's members
     */
    public Collection<UserEntity> getMembers(ProjectEntity project) {
        return userRepository.findByGitlabIdIn(project.getMemberships().stream()
            .map(membership -> membership.getIdentity().getUserGitlabId())
            .collect(toList()));
    }
}
