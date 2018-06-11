package due.debugchain.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.client.RestOperations;

import java.util.*;

/**
 * Token services for validating OAuth2 tokens from GitLab.
 */
@Component
@RequiredArgsConstructor
public class GitLabTokenServices implements ResourceServerTokenServices {

    private final RestOperations rest;

    @Value("${oauth2.gitlab.userEndpoint:http://localhost/api/v4/user}")
    private String userEndpoint;

    @Override
    public OAuth2Authentication loadAuthentication(String accessToken) throws AuthenticationException, InvalidTokenException {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", accessToken));
        GitLabUser gitLabUser = rest
            .exchange(userEndpoint, HttpMethod.GET, new HttpEntity<>(headers), GitLabUser.class)
            .getBody();
        Assert.notNull(gitLabUser.getId(), "GitLab user requires a valid ID");
        DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(accessToken);
        HashSet<String> scope = new HashSet<>(Collections.singleton("api"));
        token.setScope(scope);
        token.setTokenType("access_token");
        List<GitLabUser.Authority> authorities = new ArrayList<>();
        authorities.add(GitLabUser.Authority.USER);
        if (gitLabUser.isAdmin()) {
            authorities.add(GitLabUser.Authority.ADMIN);
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
