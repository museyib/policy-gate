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
        return permissions.stream()
                .map(item -> {
                    AppPermission appPermission = new AppPermission();
                    appPermission.setAppRole(permissible instanceof AppRole ? (AppRole) permissible : null);
                    appPermission.setAppUser(permissible instanceof AppUser ? (AppUser) permissible : null);
                    appPermission.setResource(mapToResource(item.resourceId()));
                    appPermission.setAction(item.action());
                    return appPermission;
                })
                .collect(Collectors.toSet());
    }

    public Set<PermissionRequestItem> toDto(Set<AppPermission> permissions) {
        return permissions.stream()
                .map(dto -> new PermissionRequestItem(dto.getResource().getId(), dto.getAction()))
                .collect(Collectors.toSet());
    }

    Resource mapToResource(Long value) {
        return value == 0 ? null : entityManager.getReference(Resource.class, value);
    }
}
