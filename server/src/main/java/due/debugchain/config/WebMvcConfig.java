package due.debugchain.config;

import due.debugchain.api.ProjectResolver;
import due.debugchain.api.UserResolver;
import due.debugchain.persistence.ProjectService;
import due.debugchain.persistence.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * Custom Spring Web MVC config.
 */
@Configuration
@EnableWebMvc
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final ProjectService projectService;
    private final UserService userService;

    @Override
    public void addArgumentResolvers(
        List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new ProjectResolver(projectService));
        argumentResolvers.add(new UserResolver(userService));
    }
}
