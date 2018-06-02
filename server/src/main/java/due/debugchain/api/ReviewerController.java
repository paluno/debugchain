package due.debugchain.api;

import due.debugchain.api.dto.ReviewerAddress;
import due.debugchain.persistence.ProjectService;
import due.debugchain.persistence.ReviewerService;
import due.debugchain.persistence.entities.ProjectEntity;
import due.debugchain.persistence.entities.ReviewerEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Collection;

/**
 * Endpoint for operations on reviewers.
 */
@RestController
@RequestMapping("/projects/{projectId}/reviewers")
@RequiredArgsConstructor
public class ReviewerController {

    private final ProjectService projectService;

    private final ReviewerService reviewerService;

    /**
     * Saves a reviewer to a project by mapping their GitLab ID and Ethereum address accordingly.
     *
     * @param project project to attach reviewer to
     * @param address reviewer's ethereum address
     */
    @PostMapping
    @Transactional
    public void saveAddress(ProjectEntity project, @RequestBody @Valid ReviewerAddress address) {
        Long reviewerId = 1L; // TODO: retrieve from login
        ReviewerEntity reviewer = reviewerService.createReviewer(reviewerId, address.getAddress());
        projectService.addReviewer(project, reviewer);
    }

    /**
     * Resolves all reviewers attached to a project.
     *
     * @param project project whose reviewers to resolve
     * @return project's reviewers
     */
    @GetMapping
    public Collection<ReviewerEntity> getReviewers(ProjectEntity project) {
        return project.getReviewers();
    }
}
