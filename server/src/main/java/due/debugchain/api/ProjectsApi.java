/**
 * NOTE: This class is auto generated by the swagger code generator program (2.3.1).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package due.debugchain.api;

import due.debugchain.api.dto.ProjectResource;
import io.swagger.annotations.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(value = "${server.restPath}/projects", description = "the projects API")
@RequestMapping("${server.restPath}")
public interface ProjectsApi {

    @ApiOperation(value = "Add a project", nickname = "addProject", notes = "Add a project", response = ProjectResource.class, tags={ "projects", })
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Project was added successfully", response = ProjectResource.class),
            @ApiResponse(code = 400, message = "ResponseBody does not contain a valid project or wrong HTTP-Headers") })
    @RequestMapping(value = "/projects",
            consumes = { MediaType.APPLICATION_JSON_VALUE },
            method = RequestMethod.POST)
    ResponseEntity<Void> addProject(@ApiParam(value = "The project to be added", required = true) @Valid @RequestBody ProjectResource project);


    @ApiOperation(value = "Get a project", nickname = "getProjectById", notes = "Get a project by id", response = ProjectResource.class, tags={ "projects", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Project was returned successfully", response = ProjectResource.class),
        @ApiResponse(code = 404, message = "No project with this id found") })
    @RequestMapping(value = "/projects/{projectId}",
        produces = { MediaType.APPLICATION_JSON_VALUE },
        method = RequestMethod.GET)
    ResponseEntity<ProjectResource> getProjectById(@ApiParam(value = "The id of the project", required = true) @PathVariable("projectId") long projectId);

    @ApiOperation(value = "Get all projects", nickname = "getProjects", notes = "Get all projects which are tracked by a debugchain-contract", response = ProjectResource.class, responseContainer = "List", tags={ "projects", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Projects were returned successfully", response = ProjectResource.class, responseContainer = "List"),
        @ApiResponse(code = 404, message = "No issues found") })
    @RequestMapping(value = "/projects",
        produces = { MediaType.APPLICATION_JSON_VALUE },
        method = RequestMethod.GET)
    ResponseEntity<List<ProjectResource>> getProjects(@ApiParam(value = "A searchterm for searching and filtering the projects") @Valid @RequestParam(value = "searchterm", required = false) String searchterm);

}