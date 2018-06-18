package due.debugchain.api;

import due.debugchain.api.dto.MembershipRequest;
import due.debugchain.api.dto.UserResource;
import due.debugchain.persistence.entities.ProjectEntity;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;


@RequestMapping("${server.restPath}/projects/{projectId}/members")
public interface ProjectMemberAPI {

    @ApiOperation(value = "Add logged in user to the reviewers of a project", nickname = "addMember", notes = "Add logged user to the reviwer list of a project", tags={ "projects", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Added user successfully to reviewer list"),
            @ApiResponse(code = 404, message = "No project was found.") })
    @PostMapping
    void addMember(ProjectEntity project, Authentication authentication, @RequestBody MembershipRequest request);

    @ApiOperation(value = "Get the reviewers of a project", nickname = "getMembers", notes = "Get the reviewers of a project, which are assigned for this project", response = UserResource.class, responseContainer = "List", tags={ "projects", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Reviewers were returned successfully", response = UserResource.class, responseContainer = "Collection"),
            @ApiResponse(code = 404, message = "No reviewers assigned or no project with this id found") })
    @GetMapping
    Collection<UserResource> getMembers(ProjectEntity project);

}
