package due.debugchain.api;

import due.debugchain.IntegrationTest;
import due.debugchain.persistence.entities.UserEntity;
import due.debugchain.persistence.repositories.UserRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProfileControllerTest extends IntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void saveProfile() throws Exception {
        String address = "0xde0b295669a9fd93d5f28d9ec85e40f4cb697bae";
        String addressJson = new JSONObject()
            .put("address", address)
            .toString();
        mockMvc.perform(post("/api/profile")
            .with(userToken(123L))
            .content(addressJson)
            .contentType(APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());
        UserEntity member = userRepository.findById(123L)
            .orElseThrow(IllegalStateException::new);
        assertThat(member.getAddress().toString()).isEqualTo(address);
    }

    @Test
    public void saveProfileWithoutLogin() throws Exception {
        String address = "0xde0b295669a9fd93d5f28d9ec85e40f4cb697bae";
        String addressJson = new JSONObject()
            .put("address", address)
            .toString();
        mockMvc.perform(post("/api/profile")
            .content(addressJson)
            .contentType(APPLICATION_JSON_UTF8))
            .andExpect(status().isUnauthorized());
    }

    @Test
    public void getMemberships() throws Exception {
        mockMvc.perform(get("/api/profile/memberships")
            .with(userToken()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", iterableWithSize(1)))
            .andExpect(jsonPath("$[?(@.userGitlabId == 676 && @.projectGitlabId == 999 && @.reviewer == true)]").exists());
    }

    @Test
    @Transactional
    public void saveMemberships() throws Exception {
        String json = new JSONArray()
            .put(new JSONObject()
                .put("reviewer", false)
                .put("projectGitlabId", 998L)
                .put("userGitlabId", USER_ID)
            )
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
        UserEntity member = userRepository.findById(USER_ID)
            .orElseThrow(IllegalStateException::new);
        assertThat(member.getMemberships()).hasSize(2);

        assertThat(member.getMemberships())
            .anySatisfy(m -> {
                assertThat(m.isReviewer()).isTrue();
                assertThat(m.getIdentity().getProjectGitlabId()).isEqualTo(999L);
                assertThat(m.getIdentity().getUserGitlabId()).isEqualTo(USER_ID);
            });
        assertThat(member.getMemberships()).anySatisfy(m -> {
            assertThat(m.isReviewer()).isFalse();
            assertThat(m.getIdentity().getProjectGitlabId()).isEqualTo(998L);
            assertThat(m.getIdentity().getUserGitlabId()).isEqualTo(USER_ID);
        });
    }
}