package due.debugchain.api;

import due.debugchain.IntegrationTest;
import due.debugchain.contracts.DebugChain;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static java.math.BigInteger.valueOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class IssuesControllerTest extends IntegrationTest {

    private DebugChain contract;

    @Before
    public void setup() throws Exception {
        contract = deployContract();
        contract.donate(valueOf(1L), valueOf(1L)).send();
        contract.donate(valueOf(2L), valueOf(1L)).send();
    }


    @Test // TODO remove
    public void addIssues() throws Exception{
        List issues = contract.getIssueLookup().send();
        assertThat(issues.size()).isEqualTo(2);
    }

    @Test
    @Ignore // TODO implement
    public void getIssuesWithId() throws Exception {
        mockMvc.perform(get("/api/projects/1/issues/1")
                .with(userToken()))
                .andExpect(status().isOk());

    }

    @Test
    @Ignore // TODO implement
    public void getIssueList() throws Exception {
        mockMvc.perform(get("/api/projects/1/issues")
                .with(userToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", iterableWithSize(2)));
    }



}
