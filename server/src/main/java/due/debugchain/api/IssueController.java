package due.debugchain.api;

import due.debugchain.api.dto.IssueResource;
import due.debugchain.api.mappers.IssueMapper;
import due.debugchain.chain.ContractService;
import due.debugchain.persistence.ProjectService;
import due.debugchain.persistence.entities.ProjectEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("${server.restPath}/projects/{projectId}/issues")
public class IssueController {

    private final ProjectService projectService;

    private final ContractService contractService;

    private final IssueMapper issueMapper;

    @GetMapping
    public Collection<IssueResource> getIssues(@PathVariable("projectId") long projectId) {

        
        return null;
    }

    @GetMapping("/{issueId}")
    public IssueResource getIssue(@PathVariable("projectId") long projectId, @PathVariable("issueId") long issueId) {

        Optional<ProjectEntity> project = projectService.getProject(projectId);

        return project
                .map(projectEntity -> issueMapper.entityToResource(contractService.getIssue(projectEntity.getAddress(), issueId)))
                .orElseThrow(ProjectNotFoundException::new);

    }
}
