package due.debugchain.api;

import due.debugchain.api.dto.AddressResource;
import due.debugchain.api.dto.MembershipResource;
import due.debugchain.api.dto.UserResource;
import due.debugchain.api.mappers.MembershipMapper;
import due.debugchain.api.mappers.UserMapper;
import due.debugchain.chain.ContractService;
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

        long withdrawals = contractService.getUserWithdrawals(project.getAddress(), currentUser.getAddress().toString()).longValue();

        UserResource userResource = userMapper.entityToResource(currentUser);
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
}
