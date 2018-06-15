package due.debugchain.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import due.debugchain.api.dto.ProjectResource;
import due.debugchain.api.mappers.ProjectMapper;
import due.debugchain.persistence.ProjectService;
import due.debugchain.persistence.entities.ProjectEntity;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ProjectsApiController implements ProjectsApi {

    private static final Logger log = LoggerFactory.getLogger(ProjectsApiController.class);

    private final ProjectService projectService;

    private final ProjectMapper projectMapper;

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Override
    public ResponseEntity<Void> addProject(@ApiParam(value = "The project to be added", required = true) @Valid @RequestBody ProjectResource project) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            projectService.addProject(projectMapper.resourceToEntity(project));
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<ProjectResource> getProjectById(@ApiParam(value = "The id of the project", required = true) @PathVariable("projectId") long projectId) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            Optional<ProjectEntity> project = projectService.getProject(projectId);

            if (project.isPresent()) {
                return new ResponseEntity<>(projectMapper.entityToResource(project.get()), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<ProjectResource>> getProjects(@ApiParam(value = "A searchterm for searching and filtering the projects") @Valid @RequestParam(value = "searchterm", required = false) String searchterm) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {

            List<ProjectResource> projects = new ArrayList<>();
            projectService.getAll().forEach(p -> projects.add(projectMapper.entityToResource(p)));

            return new ResponseEntity<List<ProjectResource>>(projects, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
