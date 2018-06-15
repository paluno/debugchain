package due.debugchain.api;


import due.debugchain.IntegrationTest;
import due.debugchain.persistence.entities.ProjectEntity;
import due.debugchain.persistence.repositories.ProjectRepository;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProjectApiControllerTest extends IntegrationTest {

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    public void saveProject() throws Exception {
        Long gitlabId = 19990L;
        String address = "0xde0b295669a9fd93d5f28d9ec85e40f4cb697bae";
        String projectJson = new JSONObject()
                .put("gitlabId",gitlabId)
                .put("address", address)
                .toString();
        mockMvc.perform(post("/api/projects")
                .with(userToken())
                .accept(APPLICATION_JSON)
                .content(projectJson)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isCreated());
        ProjectEntity project = projectRepository.findById(gitlabId)
                .orElseThrow(IllegalStateException::new);
        assertThat(project.getAddress()).isEqualTo(address);
        assertThat(project.getGitlabId()).isEqualTo(gitlabId);
    }

    @Test
    public void saveProjectWithoutLogin() throws Exception {
        Long gitlabId = 19990L;
        String address = "0xde0b295669a9fd93d5f28d9ec85e40f4cb697bae";
        String projectJson = new JSONObject()
                .put("gitlabId",gitlabId)
                .put("address", address)
                .toString();
        mockMvc.perform(post("/api/projects")
                .content(projectJson)
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isUnauthorized());
    }

}