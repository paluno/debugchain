package due.debugchain.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiParam;
import due.debugchain.api.model.Project;
import due.debugchain.api.model.ProjectReviewer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-06-05T11:48:51.982+02:00")

@Controller
public class ProjectsApiController implements ProjectsApi {

    private static final Logger log = LoggerFactory.getLogger(ProjectsApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public ProjectsApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<Project> getProjectById(@ApiParam(value = "The id of the project",required=true) @PathVariable("projectId") Integer projectId) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<Project>(objectMapper.readValue("{  \"issuesEnabled\" : true,  \"visibility\" : \"public\",  \"webUrl\" : \"webUrl\",  \"name\" : \"name\",  \"description\" : \"description\",  \"id\" : 0,  \"maintainer\" : 6}", Project.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<Project>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<Project>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<List<ProjectReviewer>> getProjectReviewers(@ApiParam(value = "The id of the project",required=true) @PathVariable("projectId") Integer projectId) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<ProjectReviewer>>(objectMapper.readValue("[ {  \"id\" : 0,  \"projectId\" : 6,  \"userId\" : 1}, {  \"id\" : 0,  \"projectId\" : 6,  \"userId\" : 1} ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<ProjectReviewer>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<ProjectReviewer>>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<List<Project>> getProjects(@ApiParam(value = "A searchterm for searching and filtering the projects") @Valid @RequestParam(value = "searchterm", required = false) String searchterm) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<Project>>(objectMapper.readValue("[ {  \"issuesEnabled\" : true,  \"visibility\" : \"public\",  \"webUrl\" : \"webUrl\",  \"name\" : \"name\",  \"description\" : \"description\",  \"id\" : 0,  \"maintainer\" : 6}, {  \"issuesEnabled\" : true,  \"visibility\" : \"public\",  \"webUrl\" : \"webUrl\",  \"name\" : \"name\",  \"description\" : \"description\",  \"id\" : 0,  \"maintainer\" : 6} ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<Project>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<Project>>(HttpStatus.NOT_IMPLEMENTED);
    }

}
