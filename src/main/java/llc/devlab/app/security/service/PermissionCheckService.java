package llc.devlab.app.security.service;

import llc.devlab.app.security.model.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import static llc.devlab.app.security.model.enums.UserType.ADMIN;

@Service
@RequiredArgsConstructor
@Slf4j
public class PermissionCheckService {
    private final PermissionService permissionService;
    private final UserService userService;

    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (authentication.getPrincipal() instanceof UserDetails userDetails) {
            UserDto appUser = userService.getByEmail(userDetails.getUsername());
            String permissionCode = targetDomainObject + ":" + permission;
            log.debug("Checking permission {} for user {}", permissionCode, appUser.email());

            boolean isAdmin = ADMIN.equals(appUser.userType());

            return isAdmin || permissionService.getUserPermission(appUser.id())
                    .permissions()
                    .stream()
                    .anyMatch(appPermission -> appPermission.getPermissionCode().equals(permissionCode));
        } else
            return false;
    }
}

