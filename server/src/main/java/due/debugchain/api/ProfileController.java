package due.debugchain.api;

import due.debugchain.api.dto.AddressResource;
import due.debugchain.api.dto.IssueResource;
import due.debugchain.api.dto.MembershipResource;
import due.debugchain.api.dto.UserResource;
import due.debugchain.api.mappers.IssueMapper;
import due.debugchain.api.mappers.MembershipMapper;
import due.debugchain.api.mappers.UserMapper;
import due.debugchain.chain.ContractService;
import due.debugchain.chain.IssueStruct;
import due.debugchain.persistence.ProjectService;
import due.debugchain.persistence.UserService;
import due.debugchain.persistence.entities.MembershipEntity;
import due.debugchain.persistence.entities.ProjectEntity;
import due.debugchain.persistence.entities.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.web3j.abi.datatypes.Address;

import javax.validation.Valid;
import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("${server.restPath}/profile")
public class ProfileController {

    private final UserService userService;

    private final ProjectService projectService;

    private final ContractService contractService;

    private final UserMapper userMapper;

    private final MembershipMapper membershipMapper;

    private final IssueMapper issueMapper;

    @PostMapping
    public UserResource saveProfile(@RequestBody @Valid AddressResource addressResource, UserEntity currentUser) {
        currentUser.setAddress(new Address(addressResource.getAddress()));
        return userMapper.entityToResource(userService.updateUser(currentUser));
    }

    @GetMapping
    public UserResource getProfile(UserEntity currentUser) {
        return userMapper.entityToResource(currentUser);
    }

    @GetMapping("/withdrawals/{projectId}")
    public UserResource getProfileWithdrawals(UserEntity currentUser, ProjectEntity project) {

        UserResource userResource = userMapper.entityToResource(currentUser);

        //if user address is not set
        if (currentUser.getAddress() == null) {
            userResource.setPendingWithdrawals(0L);
            return userResource;
        }

        long withdrawals = contractService.getUserWithdrawals(project.getAddress(), currentUser.getAddress().toString()).longValue();
        userResource.setPendingWithdrawals(withdrawals);

        return userResource;
    }

    @PostMapping("/memberships")
    public void updateMemberships(@RequestBody Collection<MembershipResource> memberships, UserEntity currentUser) {
        memberships.forEach(m -> Assert.isTrue(currentUser.getGitlabId().equals(m.getUserGitlabId()), "Can only modify owned memberships"));
        userService.saveMemberships(currentUser, membershipMapper.resourcesToEntities(memberships));
    }

    @GetMapping("/memberships")
    public Collection<MembershipResource> getMemberships(UserEntity currentUser) {
        Collection<MembershipEntity> memberships = currentUser.getMemberships();
        return membershipMapper.entitiesToResources(memberships);
    }

    @GetMapping("/assignedIssues")
    public Collection<IssueResource> getAssignedIssues(UserEntity currentUser) {
        return getAllIssues()
            .filter(issue -> issue.getReviewers().contains(currentUser.getAddress()) || issue.getDeveloper().equals(currentUser.getAddress()))
            .map(issueMapper::entityToResource)
            .collect(toList());
    }

    private Stream<IssueStruct> getAllIssues() {
        // get all projects
        return StreamSupport.stream(projectService.getAll().spliterator(), false)
            // issue ids per project
            .map(projectEntity -> contractService.getIssueIdList(projectEntity.getAddress())
                .stream()
                // issue per issueId
                .map(issueId -> contractService.getIssue(projectEntity.getAddress(), issueId.longValue())))
            // flatten stream
            .flatMap(Function.identity());
    }
}
