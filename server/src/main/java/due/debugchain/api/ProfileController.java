package due.debugchain.api;

import due.debugchain.api.dto.MembershipResource;
import due.debugchain.api.dto.UserResource;
import due.debugchain.api.mappers.MembershipMapper;
import due.debugchain.api.mappers.UserMapper;
import due.debugchain.auth.GitLabUser;
import due.debugchain.persistence.UserService;
import due.debugchain.persistence.entities.MembershipEntity;
import due.debugchain.persistence.entities.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.web3j.abi.datatypes.Address;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("${server.restPath}/profile")
public class ProfileController {

    private final UserService userService;

    private final UserMapper userMapper;

    private final MembershipMapper membershipMapper;

    @PostMapping
    public UserResource saveProfile(@RequestBody @Valid UserResource userResource, Authentication authentication) {
        GitLabUser principal = (GitLabUser) authentication.getPrincipal();
        Long gitlabId = principal.getId();
        UserEntity newUser = new UserEntity();
        newUser.setAddress(new Address(userResource.getAddress()));
        newUser.setGitlabId(gitlabId);
        return userMapper.entityToResource(userService.createUser(newUser));
    }


    @PostMapping("/memberships")
    public void updateMemberships(@RequestBody Collection<MembershipResource> memberships) {
        UserEntity currentUser = currentUser();
        memberships.forEach(m -> Assert.isTrue(currentUser.getGitlabId().equals(m.getUserGitlabId()), "Can only modify owned memberships"));
        userService.saveMemberships(currentUser, membershipMapper.resourcesToEntities(memberships));
    }

    @GetMapping("/memberships")
    public Collection<MembershipResource> getMemberships() {
        Collection<MembershipEntity> memberships = currentUser().getMemberships();
        return membershipMapper.entitiesToResources(memberships);
    }

    private UserEntity currentUser() {
        return userService.currentUser()
            .orElseThrow(() -> new RuntimeException("Could not resolve current user"));
    }
}
