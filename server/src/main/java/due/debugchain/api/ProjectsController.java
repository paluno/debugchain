package due.debugchain.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import due.debugchain.api.dto.ProjectResource;
import due.debugchain.api.dto.UserResource;
import due.debugchain.api.mappers.ProjectMapper;
import due.debugchain.api.mappers.UserMapper;
import due.debugchain.persistence.ProjectService;
import due.debugchain.persistence.UserService;
import due.debugchain.persistence.entities.ProjectEntity;
import due.debugchain.persistence.entities.UserEntity;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
public class ProjectsController implements ProjectsApi {

    private final ProjectService projectService;

    private final ProjectMapper projectMapper;

    private final UserMapper userMapper;

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Override
    public ResponseEntity<Void> addProject(@ApiParam(value = "The project to be added", required = true) @Valid @RequestBody ProjectResource project) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            projectService.addProject(projectMapper.resourceToEntity(project));
            return new ResponseEntity<>(CREATED);
        }
        return new ResponseEntity<>(BAD_REQUEST);
    }

    public ResponseEntity<ProjectResource> getProjectById(@ApiParam(value = "The id of the project", required = true) @PathVariable("projectId") long projectId) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            Optional<ProjectEntity> project = projectService.getProject(projectId);
            return project
                    .map(projectEntity -> ResponseEntity.ok(projectMapper.entityToResource(projectEntity)))
                    .orElseGet(() -> new ResponseEntity<>(NOT_FOUND));
        }
        return new ResponseEntity<>(BAD_REQUEST);
    }

    public ResponseEntity<List<ProjectResource>> getProjects(@ApiParam(value = "A searchterm for searching and filtering the projects") @Valid @RequestParam(value = "searchterm", required = false) String searchterm) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {

            List<ProjectResource> projects = new ArrayList<>();
            projectService.getAll().forEach(p -> projects.add(projectMapper.entityToResource(p)));

            return new ResponseEntity<List<ProjectResource>>(projects, OK);
        }
        return new ResponseEntity<>(BAD_REQUEST);
    }

    public Collection<UserResource> getReviewers(ProjectEntity project) {

        return userMapper.entitiesToResources(projectService.getAllReviewers(project.getGitlabId()));
    }
}
