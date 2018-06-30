package due.debugchain.api;


import due.debugchain.IntegrationTest;
import due.debugchain.persistence.entities.ProjectEntity;
import due.debugchain.persistence.repositories.ProjectRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProjectsControllerTest extends IntegrationTest {

    @Autowired
    private ProjectRepository projectRepository;

    @Before
    @Transactional
    public void setup() throws Exception {
        mockMvc.perform(get("/api/profile")
                .with(userToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gitlabId", equalTo(676)))
                .andExpect(jsonPath("$.address", equalTo("0x99861c8068bfe2e0a5137e16d23a648962c79b5c")));

        String json = new JSONArray()
                .put(new JSONObject()
                        .put("reviewer", true)
                        .put("projectGitlabId", 999L)
                        .put("userGitlabId", USER_ID)
                )
                .toString();
        mockMvc.perform(post("/api/profile/memberships")
                .with(userToken())
                .content(json)
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    public void saveProject() throws Exception {
        Long gitlabId = 19990L;
        String address = "0xde0b295669a9fd93d5f28d9ec85e40f4cb697bae";
        String projectJson = new JSONObject()
                .put("gitlabId", gitlabId)
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
                .put("gitlabId", gitlabId)
                .put("address", address)
                .toString();
        mockMvc.perform(post("/api/projects")
                .content(projectJson)
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void getProjectReviewers() throws Exception {
        mockMvc.perform(get("/api/projects/999/reviewers")
                .with(userToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", iterableWithSize(3)))
                .andExpect(jsonPath("$[0].address", equalTo("0x99861c8068bfe2e0a5137e16d23a648962c79b5c")))
                .andExpect(jsonPath("$[0].gitlabId", equalTo(676)))
                .andExpect(jsonPath("$[1].address", equalTo("0x2e9ea27add78cd06c6bfb9bfd9966f726bc273a0")))
                .andExpect(jsonPath("$[1].gitlabId", equalTo(677)));
    }

}