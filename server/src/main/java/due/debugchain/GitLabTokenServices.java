package due.debugchain;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.util.Assert;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;

/**
 * Token services for validating OAuth2 tokens from GitLab.
 */
public class GitLabTokenServices implements ResourceServerTokenServices {

    private RestOperations restTemplate = new RestTemplate();

    private final String userEndpoint;

    /**
     * Default constructor for specifying GitLab's endpoint for retrieving current user.
     *
     * @param userEndpoint GitLab's endpoint for retrieving current user
     */
    public GitLabTokenServices(String userEndpoint) {
        this.userEndpoint = userEndpoint;
        ((RestTemplate) restTemplate).setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            // Ignore 400
            public void handleError(ClientHttpResponse response) throws IOException {
                if (response.getRawStatusCode() != HttpStatus.BAD_REQUEST.value()) {
                    super.handleError(response);
                }
            }
        });
    }

    @Override
    public OAuth2Authentication loadAuthentication(String accessToken) throws AuthenticationException, InvalidTokenException {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", accessToken));
        GitLabUser gitLabUser = restTemplate
            .exchange(userEndpoint, HttpMethod.GET, new HttpEntity<>(headers), GitLabUser.class)
            .getBody();
        Assert.notNull(gitLabUser.getId(), "GitLab user requires a valid ID");
        DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(accessToken);
        HashSet<String> scope = new HashSet<>(Collections.singleton("api"));
        token.setScope(scope);
        token.setTokenType("access_token");
        List<GitLabUser.Authority> authorities = new ArrayList<>();
        authorities.add(new GitLabUser.Authority("user"));
        if (gitLabUser.isAdmin()) {
            authorities.add(new GitLabUser.Authority("admin"));
        }
        Authentication authentication = new UsernamePasswordAuthenticationToken(gitLabUser, "N/A", authorities);
        OAuth2Request request = new OAuth2Request(new HashMap<>(), null, null,
            true, scope, null, null, null, null);
        return new OAuth2Authentication(request, authentication);
    }

    @Override
    public OAuth2AccessToken readAccessToken(String accessToken) {
        throw new UnsupportedOperationException("Not supported: read access token");
    }
}
