package llc.devlab.app.security.service;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@RequiredArgsConstructor
public class CustomPermissionEvaluator implements PermissionEvaluator {
    private final PermissionCheckService permissionCheckService;

    @Override
    public boolean hasPermission(@NonNull Authentication authentication, @NonNull Object targetDomainObject, @NonNull Object permission) {
        return permissionCheckService.hasPermission(authentication, targetDomainObject, permission);
    }

    @Override
    public boolean hasPermission(@NonNull Authentication authentication, @NonNull Serializable targetId, @NonNull String targetType, @NonNull Object permission) {
        return permissionCheckService.hasPermission(authentication, targetType, permission);
    }
}
