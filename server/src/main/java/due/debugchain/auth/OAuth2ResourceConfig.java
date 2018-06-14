package due.debugchain.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

import static org.springframework.http.HttpMethod.GET;

/**
 * Resource server configuration for OAuth2 integration with GitLab.
 */
@Configuration
@EnableResourceServer
@RequiredArgsConstructor
public class OAuth2ResourceConfig extends ResourceServerConfigurerAdapter {

    private final GitLabTokenServices tokenServices;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .httpBasic().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers(// dev paths
                "/api-docs/**",
                "/swagger-ui.html",
                "/webjars/springfox-swagger-ui/**").permitAll()
            .antMatchers(GET, "/projects/*/members").permitAll()
            //.anyRequest().authenticated();
            .anyRequest().permitAll();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.tokenServices(tokenServices);
    }
}