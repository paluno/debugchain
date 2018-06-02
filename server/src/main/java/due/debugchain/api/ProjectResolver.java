package due.debugchain.api;

import due.debugchain.persistence.ProjectService;
import due.debugchain.persistence.entities.ProjectEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static org.springframework.web.servlet.HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE;

/**
 * Resolves method argument {@link ProjectEntity} with argument name 'project' for request mappings with path variable 'projectId'.
 */
@RequiredArgsConstructor
public class ProjectResolver implements HandlerMethodArgumentResolver {

    private final ProjectService projectService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(ProjectEntity.class)
            && parameter.getParameterName().equals("project");
    }

    @Override
    public ProjectEntity resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer,
                                         NativeWebRequest webRequest, WebDataBinderFactory webDataBinderFactory) {
        HttpServletRequest httpServletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        Map pathVariables = (Map) httpServletRequest.getAttribute(URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        Long projectId = Long.valueOf((String) pathVariables.get("projectId"));
        return projectService
            .getProject(projectId)
            .orElseThrow(ProjectNotFoundException::new);
    }
}
