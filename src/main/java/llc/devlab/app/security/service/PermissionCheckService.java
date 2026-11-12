package llc.devlab.app.security.service;

import llc.devlab.app.config.CacheConfig;
import llc.devlab.app.security.model.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import static llc.devlab.app.security.model.enums.UserType.ADMIN;

@Service
@RequiredArgsConstructor
public class PermissionCheckService {
    private final PermissionService permissionService;
    private final UserService userService;

    @Cacheable(value = CacheConfig.CacheNames.HAS_PERMISSION)
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (authentication.getPrincipal() instanceof UserDetails userDetails) {
            UserDto appUser = userService.getByEmail(userDetails.getUsername());
            String permissionCode = targetDomainObject + ":" + permission;

            boolean isAdmin = ADMIN.equals(appUser.userType());

            return isAdmin || permissionService.getUserPermission(appUser.id())
                    .permissions()
                    .stream()
                    .anyMatch(appPermission -> appPermission.getPermissionCode().equals(permissionCode));
        } else
            return false;
    }
}

