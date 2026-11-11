package llc.devlab.app.security.mapper;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import llc.devlab.app.exception.ResourceNotFoundException;
import llc.devlab.app.security.model.dto.RoleDto;
import llc.devlab.app.security.model.dto.UserDto;
import llc.devlab.app.security.model.dto.UserUpdateRequest;
import llc.devlab.app.security.model.entity.AppRole;
import llc.devlab.app.security.model.entity.AppUser;
import org.mapstruct.*;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    @PersistenceContext
    private EntityManager entityManager;

    public abstract UserDto toDto(AppUser appUser);

    public abstract RoleDto toDto(AppRole role);

    public abstract Set<RoleDto> toRoleDto(Set<AppRole> roles);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateEntity(UserUpdateRequest request, @MappingTarget AppUser appUser);

    Set<AppRole> mapToRoles(Set<Long> roles) {
        if (roles == null)
            return null;
        return roles.stream()
                .map(roleId -> {
                    AppRole role = entityManager.find(AppRole.class, roleId);
                    if (role == null)
                        throw new ResourceNotFoundException("Role not found with id " + roleId);
                    return role;
                })
                .collect(Collectors.toSet());
    }
}
