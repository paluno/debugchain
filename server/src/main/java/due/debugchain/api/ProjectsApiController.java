package due.debugchain.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import due.debugchain.api.dto.ProjectResource;
import due.debugchain.api.dto.UserResource;
import due.debugchain.api.mappers.ProjectMapper;
import due.debugchain.persistence.ProjectService;
import io.swagger.annotations.ApiParam;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
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

    public ResponseEntity<ProjectResource> getProjectById(@ApiParam(value = "The id of the project",required=true) @PathVariable("projectId") Integer projectId) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<>(objectMapper.readValue("{  \"gitlabId\" : \"0\",  \"address\" : \"none\"  }", ProjectResource.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<List<UserResource>> getProjectReviewers(@ApiParam(value = "The id of the project",required=true) @PathVariable("projectId") Integer projectId) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<UserResource>>(objectMapper.readValue("[ {  \"gitlabId\" : \"0\",  \"address\" : \"none\"  }, {  \"gitlabId\" : \"0\",  \"address\" : \"none\"  } ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<List<ProjectResource>> getProjects(@ApiParam(value = "A searchterm for searching and filtering the projects") @Valid @RequestParam(value = "searchterm", required = false) String searchterm) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<ProjectResource>>(objectMapper.readValue("[ {  \"gitlabId\" : \"0\",  \"address\" : \"none\"  },  {  \"gitlabId\" : \"0\",  \"address\" : \"none\"  } ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

}
