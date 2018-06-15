package due.debugchain.api;

import due.debugchain.api.dto.MembershipRequest;
import due.debugchain.api.dto.UserResource;
import due.debugchain.api.mappers.UserMapper;
import due.debugchain.auth.GitLabUser;
import due.debugchain.persistence.ProjectService;
import due.debugchain.persistence.UserService;
import due.debugchain.persistence.entities.ProjectEntity;
import due.debugchain.persistence.entities.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
public class ProjectMemberController implements ProjectMemberAPI{

    private final ProjectService projectService;

    private final UserService userService;

    private final UserMapper userMapper;

    /**
     * Saves a user to a project by mapping their GitLab ID and Ethereum address accordingly.
     *
     * @param project project to attach user to
     * @param authentication current user authentication
     * @param request dto
     */
    @Transactional
    public void addMember(ProjectEntity project, Authentication authentication, @RequestBody MembershipRequest request) {
        GitLabUser principal = (GitLabUser) authentication.getPrincipal();
        Long gitlabId = principal.getId();
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
     * Resolves all members associated with a project.
     *
     * @param project project whose members to resolve
     * @return project's members
     */
    public Collection<UserResource> getMembers(ProjectEntity project) {
        return userMapper.entitiesToResources(projectService.getMembers(project));
    }
}
