package due.debugchain;

import org.junit.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OAuth2ResourceConfigTest extends IntegrationTest {

    @Test
    public void denyApiWithoutToken() throws Exception {
        mockMvc.perform(get("/blocks"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    public void allowApiWithToken() throws Exception {
        mockMvc.perform(get("/blocks")
            .with(userToken()))
            .andExpect(status().isOk());
    }
}