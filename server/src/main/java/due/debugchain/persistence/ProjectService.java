package due.debugchain.persistence;

import due.debugchain.persistence.entities.ProjectEntity;
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

    public void addProject(ProjectEntity projectEntity){ projectRepository.save(projectEntity); }

    public Iterable<ProjectEntity> getAll() {
        return projectRepository.findAll();
    }
}
