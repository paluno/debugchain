package due.debugchain.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiParam;
import due.debugchain.api.model.Issue;
import due.debugchain.api.model.IssueReviewer;
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
public class IssuesApiController implements IssuesApi {

    private static final Logger log = LoggerFactory.getLogger(IssuesApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public IssuesApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<Issue> getIssueById(@ApiParam(value = "The id of the issue",required=true) @PathVariable("issueId") Integer issueId) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<Issue>(objectMapper.readValue("{  \"lockedAdress\" : \"lockedAdress\",  \"webUrl\" : \"webUrl\",  \"approve\" : false,  \"description\" : \"description\",  \"id\" : 0,  \"state\" : \"opened\",  \"title\" : \"title\",  \"complete\" : false,  \"projectId\" : 6,  \"lockedGitLabId\" : 1}", Issue.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<Issue>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<Issue>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<List<IssueReviewer>> getIssueReviewers(@ApiParam(value = "The id of the issue",required=true) @PathVariable("issueId") Integer issueId) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<IssueReviewer>>(objectMapper.readValue("[ {  \"issueId\" : 6,  \"id\" : 0,  \"state\" : \"confirmed\",  \"userId\" : 1}, {  \"issueId\" : 6,  \"id\" : 0,  \"state\" : \"confirmed\",  \"userId\" : 1} ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<IssueReviewer>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<IssueReviewer>>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<List<Issue>> getIssues(@ApiParam(value = "A searchterm for searching and filtering the issues") @Valid @RequestParam(value = "searchterm", required = false) Integer searchterm) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<Issue>>(objectMapper.readValue("[ {  \"lockedAdress\" : \"lockedAdress\",  \"webUrl\" : \"webUrl\",  \"approve\" : false,  \"description\" : \"description\",  \"id\" : 0,  \"state\" : \"opened\",  \"title\" : \"title\",  \"complete\" : false,  \"projectId\" : 6,  \"lockedGitLabId\" : 1}, {  \"lockedAdress\" : \"lockedAdress\",  \"webUrl\" : \"webUrl\",  \"approve\" : false,  \"description\" : \"description\",  \"id\" : 0,  \"state\" : \"opened\",  \"title\" : \"title\",  \"complete\" : false,  \"projectId\" : 6,  \"lockedGitLabId\" : 1} ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<Issue>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<Issue>>(HttpStatus.NOT_IMPLEMENTED);
    }

}
