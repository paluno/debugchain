package due.debugchain.persistence;

import due.debugchain.persistence.entities.ProjectEntity;
import due.debugchain.persistence.entities.ReviewerEntity;
import due.debugchain.persistence.repositories.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service for business logic regarding projects.
 */
@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

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
     * Attaches a reviewer to a project.
     *
     * @param project  project to attach to
     * @param reviewer reviewer to attach
     */
    public void addReviewer(ProjectEntity project, ReviewerEntity reviewer) {
        project.getReviewers().add(reviewer);
        projectRepository.save(project);
    }
}
