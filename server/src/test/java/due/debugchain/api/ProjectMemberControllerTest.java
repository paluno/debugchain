package due.debugchain.api;

import due.debugchain.IntegrationTest;
import due.debugchain.persistence.entities.UserEntity;
import due.debugchain.persistence.repositories.UserRepository;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProjectMemberControllerTest extends IntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void saveAddress() throws Exception {
        String address = "0xde0b295669a9fd93d5f28d9ec85e40f4cb697bae";
        String addressJson = new JSONObject()
            .put("address", address)
            .toString();
        mockMvc.perform(post("/projects/1/members")
            .with(userToken())
            .content(addressJson)
            .contentType(APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());
        UserEntity reviewer = userRepository.findById(1L)
            .orElseThrow(IllegalStateException::new);
        assertThat(reviewer.getAddress().toString()).isEqualTo(address);
    }
}