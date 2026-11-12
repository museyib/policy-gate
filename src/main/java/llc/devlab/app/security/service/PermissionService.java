package llc.devlab.app.security.service;

import llc.devlab.app.exception.ResourceNotFoundException;
import llc.devlab.app.security.mapper.PermissionMapper;
import llc.devlab.app.security.model.dto.*;
import llc.devlab.app.security.model.entity.AppPermission;
import llc.devlab.app.security.model.entity.AppRole;
import llc.devlab.app.security.model.entity.AppUser;
import llc.devlab.app.security.model.entity.Permissible;
import llc.devlab.app.security.repository.PermissionRepository;
import llc.devlab.app.security.repository.RoleRepository;
import llc.devlab.app.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermissionService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    public PermissionResponse getUserPermission(Long id) {
        Set<PermissionItem> permissions = permissionRepository.getPermissionsByUserId(id);
        return new PermissionResponse(id, permissions);
    }

    public PermissionResponse getRolePermission(Long id) {
        Set<PermissionItem> permissions = permissionRepository.getPermissionsByRoleId(id);
        return new PermissionResponse(id, permissions);
    }

    @Transactional
    public void updateRolePermission(Long roleId, RolePermissionRequest request) {
        AppRole appRole = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        processPermissionExchange(permissionMapper.toEntity(request.permissions(), appRole), appRole);
    }

    @Transactional
    public void updateUserPermission(Long userId, UserPermissionRequest request) {
        AppUser appUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        processPermissionExchange(permissionMapper.toEntity(request.permissions(), appUser), appUser);
    }

    private void processPermissionExchange(Set<AppPermission> permissionsIncoming, Permissible permissible) {
        Set<AppPermission> currentPermissions = permissible.getPermissions();
        Set<AppPermission> permissionsToAdd = permissionsIncoming.stream()
                .filter(permission -> !currentPermissions.contains(permission))
                .collect(Collectors.toSet());
        Set<AppPermission> permissionsToDelete = currentPermissions.stream()
                .filter(permission -> !permissionsIncoming.contains(permission))
                .collect(Collectors.toSet());
        permissionRepository.deleteAll(permissionsToDelete);
        permissionRepository.saveAll(permissionsToAdd);
    }
}
