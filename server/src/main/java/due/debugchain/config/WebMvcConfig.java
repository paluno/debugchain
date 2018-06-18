package due.debugchain.config;

import due.debugchain.api.ProjectResolver;
import due.debugchain.persistence.ProjectService;
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

    @Override
    public void addArgumentResolvers(
        List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new ProjectResolver(projectService));
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/", "index.html");
    }
}
