package due.debugchain.api;

import due.debugchain.IntegrationTest;
import due.debugchain.contracts.DebugChain;
import due.debugchain.persistence.ProjectService;
import due.debugchain.persistence.entities.ProjectEntity;
import due.debugchain.persistence.entities.UserEntity;
import due.debugchain.persistence.repositories.UserRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.math.BigInteger.valueOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProfileControllerTest extends IntegrationTest {

    private DebugChain contract;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectService projectService;

    @Before
    public void setup() throws Exception {
        contract = deployContract();
        contract.donate(valueOf(1L), valueOf(1L)).send();

        String adress = contract.getContractAddress();
        ProjectEntity project = new ProjectEntity();
        project.setGitlabId(999L);
        project.setAddress(adress);
        projectService.addProject(project);
    }

    @Test
    public void getProfile() throws Exception {
        mockMvc.perform(get("/api/profile")
            .with(userToken()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.gitlabId", equalTo(676)))
            .andExpect(jsonPath("$.address", equalTo("0x99861c8068bfe2e0a5137e16d23a648962c79b5c")));
    }

    @Test
    public void getProfileWithdrawals() throws Exception {
        System.out.println(mockMvc.perform(get("/api/profile/withdrawals/999")
                .with(userToken(678L)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gitlabId", equalTo(678)))
                .andExpect(jsonPath("$.address", equalTo("0x627306090abab3a6e1400e9345bc60c78a8bef57")))
                //.andExpect(jsonPath("$.pendingWithdrawals", equalTo(0)))
                .andReturn().getResponse().getContentAsString());
    }

    @Test
    public void saveProfileForNoneExistentUser() throws Exception {
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
    public void saveProfileForExistentUser() throws Exception {
        String address = "0xde0b295669a9fd93d5f28d9ec85e40f4cb697bae";
        String addressJson = new JSONObject()
            .put("address", address)
            .toString();
        mockMvc.perform(post("/api/profile")
            .with(userToken(USER_ID))
            .content(addressJson)
            .contentType(APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());
        UserEntity member = userRepository.findById(USER_ID)
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