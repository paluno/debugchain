package due.debugchain.api;

import due.debugchain.IntegrationTest;
import due.debugchain.persistence.entities.UserEntity;
import due.debugchain.persistence.repositories.UserRepository;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
        mockMvc.perform(post("/api/projects/999/members")
            .with(userToken())
            .content(addressJson)
            .contentType(APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());
        UserEntity reviewer = userRepository.findById(USER_ID)
            .orElseThrow(IllegalStateException::new);
        assertThat(reviewer.getAddress().toString()).isEqualTo(address);
    }

    @Test
    public void saveAddressWithoutLogin() throws Exception {
        String address = "0xde0b295669a9fd93d5f28d9ec85e40f4cb697bae";
        String addressJson = new JSONObject()
            .put("address", address)
            .toString();
        mockMvc.perform(post("/api/projects/999/members")
            .content(addressJson)
            .contentType(APPLICATION_JSON_UTF8))
            .andExpect(status().isUnauthorized());
    }

    @Test
    public void saveMultipleAddresses() throws Exception {
        String firstAddress = "0xde0b295669a9fd93d5f28d9ec85e40f4cb697bae";
        String firstAddressJson = new JSONObject()
            .put("address", firstAddress)
            .toString();
        mockMvc.perform(post("/api/projects/999/members")
            .with(userToken(1336L))
            .content(firstAddressJson)
            .contentType(APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());
        UserEntity reviewer = userRepository.findById(1336L)
            .orElseThrow(IllegalStateException::new);
        assertThat(reviewer.getAddress().toString()).isEqualTo(firstAddress);
        String secondAddress = "0x123f681646d4a755815f9cb19e1acc8565a0c2ac";
        String secondAddressJson = new JSONObject()
            .put("address", secondAddress)
            .toString();
        mockMvc.perform(post("/api/projects/999/members")
            .with(userToken(1337L))
            .content(secondAddressJson)
            .contentType(APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());
        reviewer = userRepository.findById(1337L)
            .orElseThrow(IllegalStateException::new);
        assertThat(reviewer.getAddress().toString()).isEqualTo(secondAddress);
    }

    @Test
    public void fetchProjectMembers() throws Exception {
        mockMvc.perform(get("/api/projects/999/members")
                .with(userToken(1336L))
                .contentType(APPLICATION_JSON_UTF8)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", iterableWithSize(2)))
            .andExpect(jsonPath("$[?(@.gitlabId == 676 && @.address == '0x99861c8068bfe2e0a5137e16d23a648962c79b5c')]").exists())
            .andExpect(jsonPath("$[?(@.gitlabId == 677 && @.address == '0x2e9ea27add78cd06c6bfb9bfd9966f726bc273a0')]").exists());
    }
}