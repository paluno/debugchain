package due.debugchain.api;

import due.debugchain.IntegrationTest;
import due.debugchain.contracts.DebugChain;
import due.debugchain.persistence.ProjectService;
import due.debugchain.persistence.entities.ProjectEntity;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static java.math.BigInteger.valueOf;
import static net.bytebuddy.matcher.ElementMatchers.is;
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
        contract = deployContract();
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
        System.out.println(mockMvc.perform(get("/api/projects/1/issues/1")
                .with(userToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id == 1 && @.donationSum == 1)]").exists())
                .andReturn().getResponse().getContentAsString());

    }

    @Test
    public void getIssueList() throws Exception {
        System.out.println(mockMvc.perform(get("/api/projects/1/issues")
                .with(userToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", iterableWithSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)).exists())
                .andExpect(jsonPath("$[0].donationSum", is(1)).exists())
                .andExpect(jsonPath("$[1].id", is(2)).exists())
                .andExpect(jsonPath("$[1].donationSum", is(1)).exists())
                .andReturn().getResponse().getContentAsString());
    }



}
