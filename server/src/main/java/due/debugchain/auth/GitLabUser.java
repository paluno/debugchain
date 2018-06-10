package due.debugchain.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
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

    /**
     * Arbitrary authority wrapper.
     */
    @AllArgsConstructor
    @Getter
    public enum Authority implements GrantedAuthority {

        /**
         * Default authority for any authenticated GitLab user.
         */
        USER("user"),

        /**
         * Authority for GitLab admins.
         */
        ADMIN("admin");

        private String authority;
    }
}
