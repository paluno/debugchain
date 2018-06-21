package due.debugchain.api;

import due.debugchain.auth.GitLabUser;
import due.debugchain.persistence.UserService;
import due.debugchain.persistence.entities.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Resolves method argument with name `currentUser` to {@link UserEntity} corresponding with current authentication.
 */
@RequiredArgsConstructor
public class UserResolver implements HandlerMethodArgumentResolver {

    private final UserService userService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserEntity.class)
            && parameter.getParameterName().equals("currentUser");
    }

    @Override
    public UserEntity resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer,
                                      NativeWebRequest webRequest, WebDataBinderFactory webDataBinderFactory) {
        GitLabUser principal = (GitLabUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long gitlabId = principal.getId();
        return userService.getUser(gitlabId)
            .orElseGet(() -> userService.createUser(new UserEntity(gitlabId)));
    }

}
