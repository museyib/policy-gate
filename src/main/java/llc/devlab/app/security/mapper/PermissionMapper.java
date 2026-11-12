package llc.devlab.app.security.mapper;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import llc.devlab.app.security.model.dto.PermissionRequestItem;
import llc.devlab.app.security.model.entity.*;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PermissionMapper {
    @PersistenceContext
    private EntityManager entityManager;

    public Set<AppPermission> toEntity(Set<PermissionRequestItem> permissions, Permissible permissible) {
        Long roleId = permissible instanceof AppRole ? permissible.getId() : 0;
        Long userId = permissible instanceof AppUser ? permissible.getId() : 0;
        return permissions.stream()
                .map(dto -> {
                    AppPermission appPermission = new AppPermission();
                    appPermission.setAppRole(mapToAppRole(roleId));
                    appPermission.setAppUser(mapToAppUser(userId));
                    appPermission.setResource(mapToResource(dto.resourceId()));
                    appPermission.setAction(dto.action());
                    return appPermission;
                })
                .collect(Collectors.toSet());
    }

    public Set<PermissionRequestItem> toDto(Set<AppPermission> permissions) {
        return permissions.stream()
                .map(dto -> new PermissionRequestItem(dto.getResource().getId(), dto.getAction()))
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
