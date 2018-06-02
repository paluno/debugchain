package due.debugchain;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.codehaus.jackson.annotate.JsonProperty;
import org.springframework.security.core.GrantedAuthority;

/**
 * GitLab user model.
 */
@Data
public class GitLabUser {
    private Long id;
    private String name;
    private String username;
    private String state;
    private String email;
    @JsonProperty("is_admin")
    private boolean isAdmin;

    @AllArgsConstructor
    static class Authority implements GrantedAuthority {
        private String authority;

        @Override
        public String getAuthority() {
            return authority;
        }
    }
}
