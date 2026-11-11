package llc.devlab.app.security.mapper;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import llc.devlab.app.security.model.dto.RolePermissionRequest;
import llc.devlab.app.security.model.dto.UserPermissionRequest;
import llc.devlab.app.security.model.entity.AppPermission;
import llc.devlab.app.security.model.entity.AppRole;
import llc.devlab.app.security.model.entity.AppUser;
import llc.devlab.app.security.model.entity.Resource;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PermissionMapper {
    @PersistenceContext
    private EntityManager entityManager;

    public Set<AppPermission> toEntity(Long roleId, RolePermissionRequest request) {
        return request.permissions().stream()
                .map(dto -> {
                    AppPermission appPermission = new AppPermission();
                    appPermission.setAppRole(mapToAppRole(roleId));
                    appPermission.setResource(mapToResource(dto.resourceId()));
                    appPermission.setAction(dto.action());
                    return appPermission;
                })
                .collect(Collectors.toSet());
    }

    public Set<AppPermission> toEntity(Long userId, UserPermissionRequest request) {
        return request.permissions().stream()
                .map(dto -> {
                    AppPermission appPermission = new AppPermission();
                    appPermission.setAppUser(mapToAppUser(userId));
                    appPermission.setResource(mapToResource(dto.resourceId()));
                    appPermission.setAction(dto.action());
                    return appPermission;
                })
                .collect(Collectors.toSet());
    }

    AppRole mapToAppRole(Long value) {
        return value == 0 ? null : entityManager.getReference(AppRole.class, value);
    }
    AppUser mapToAppUser(Long value) {
        return value == 0 ? null : entityManager.getReference(AppUser.class, value);
    }
    Resource mapToResource(Long value) {
        return value == 0 ? null : entityManager.getReference(Resource.class, value);
    }
}
