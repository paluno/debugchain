package due.debugchain.api;

import due.debugchain.IntegrationTest;
import due.debugchain.contracts.DebugChain;
import due.debugchain.persistence.ProjectService;
import due.debugchain.persistence.entities.ProjectEntity;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static java.math.BigInteger.valueOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class IssuesControllerTest extends IntegrationTest {

    private DebugChain contract;

    @Autowired
    private ProjectService projectService;

    @Before
    public void setup() throws Exception {
        contract = loadContract(999L);
        contract.donate(valueOf(1L), valueOf(1L)).send();
        contract.donate(valueOf(2L), valueOf(1L)).send();

        String adress = contract.getContractAddress();
        ProjectEntity project = new ProjectEntity();
        project.setGitlabId(1L);
        project.setAddress(adress);
        projectService.addProject(project);
    }

    @Test
    public void getIssuesWithId() throws Exception {
        mockMvc.perform(get("/api/projects/1/issues/1")
                .with(userToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id == 1 && @.donationSum == 1 && @.developer == null)]").exists());

    }

    @Test
    public void getIssueList() throws Exception {
        mockMvc.perform(get("/api/projects/1/issues")
                .with(userToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", iterableWithSize(2)))
                .andExpect(jsonPath("$[0].id", equalTo(1)))
                .andExpect(jsonPath("$[0].donationSum", equalTo(1)))
                .andExpect(jsonPath("$[0].developer", equalTo(null)))
                .andExpect(jsonPath("$[0].donators[0]", equalTo("0x627306090abab3a6e1400e9345bc60c78a8bef57")))
                .andExpect(jsonPath("$[1].id", equalTo(2)))
                .andExpect(jsonPath("$[1].donationSum", equalTo(1)))
                .andExpect(jsonPath("$[1].developer", equalTo(null)))
                .andExpect(jsonPath("$[1].donators[0]", equalTo("0x627306090abab3a6e1400e9345bc60c78a8bef57")));
    }

    @Test
    public void getIssuesWithWrongIssueId() throws Exception {
        mockMvc.perform(get("/api/projects/1/issues/12")
                .with(userToken()))
                .andExpect(status().isNotFound());

    }

}
