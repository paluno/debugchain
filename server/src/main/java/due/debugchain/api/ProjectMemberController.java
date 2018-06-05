package due.debugchain.api;

import due.debugchain.api.dto.MembershipRequest;
import due.debugchain.persistence.ProjectService;
import due.debugchain.persistence.UserService;
import due.debugchain.persistence.entities.ProjectEntity;
import due.debugchain.persistence.entities.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Collection;

/**
 * Endpoint for operations on reviewers.
 */
@RestController
@RequestMapping("/projects/{projectId}/members")
@RequiredArgsConstructor
public class ProjectMemberController {

    private final ProjectService projectService;

    private final UserService userService;

    /**
     * Saves a user to a project by mapping their GitLab ID and Ethereum address accordingly.
     *
     * @param project project to attach user to
     * @param request dto
     */
    @PostMapping
    @Transactional
    public void addMember(ProjectEntity project, @RequestBody MembershipRequest request) {
        Long gitlabId = 1L; // TODO: retrieve from login
        UserEntity user = userService.getUser(gitlabId)
            .orElseGet(() -> {
                UserEntity newUser = new UserEntity();
                Assert.notNull(request.getAddress(), "Initial membership requires a valid address");
                newUser.setAddress(request.getAddress());
                newUser.setGitlabId(gitlabId);
                return userService.createUser(newUser);
            });
        projectService.addMember(project, user, request.isReviewer());
    }

    /**
     * Resolves all reviewers attached to a project.
     *
     * @param project project whose reviewers to resolve
     * @return project's reviewers
     */
    @GetMapping
    public Collection<UserEntity> getMembers(ProjectEntity project) {
        return projectService.getMembers(project);
    }
}
