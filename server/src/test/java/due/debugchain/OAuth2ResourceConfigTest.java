package due.debugchain;

import due.debugchain.persistence.entities.ProjectEntity;
import org.json.JSONObject;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OAuth2ResourceConfigTest extends IntegrationTest {

    @Test
    public void apiAuthTestWithToken() throws Exception {
        mockMvc.perform(get("/api/projects")
                .with(userToken())
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    public void apiAuthTestWithoutToken() throws Exception {
        mockMvc.perform(get("/api/projects")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isUnauthorized());
    }
}