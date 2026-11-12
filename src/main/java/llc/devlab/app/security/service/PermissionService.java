package llc.devlab.app.security.service;

import llc.devlab.app.config.CacheConfig;
import llc.devlab.app.exception.ResourceNotFoundException;
import llc.devlab.app.security.mapper.PermissionMapper;
import llc.devlab.app.security.model.dto.*;
import llc.devlab.app.security.model.entity.*;
import llc.devlab.app.security.repository.PermissionChangeRepository;
import llc.devlab.app.security.repository.PermissionRepository;
import llc.devlab.app.security.repository.RoleRepository;
import llc.devlab.app.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

import static llc.devlab.app.config.CacheConfig.CacheNames.HAS_PERMISSION;
import static llc.devlab.app.config.CacheConfig.CacheNames.ROLE_PERMISSIONS;

@Service
@RequiredArgsConstructor
public class PermissionService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final PermissionChangeRepository permissionChangeRepository;
    private final PermissionMapper permissionMapper;

    @Cacheable(value = CacheConfig.CacheNames.USER_PERMISSIONS, key = "#userId")
    public PermissionResponse getUserPermission(Long userId) {
        Set<PermissionItemResponse> permissions = permissionRepository.getPermissionsByUserId(userId).stream()
                .map(permission -> new PermissionItemResponse(
                        permission.getId(), permission.getResourceId(), permission.getAction(), permission.getPermissionCode()))
                .collect(Collectors.toSet());
        return new PermissionResponse(userId, permissions);
    }

    @Cacheable(value = ROLE_PERMISSIONS, key = "#roleId")
    public PermissionResponse getRolePermission(Long roleId) {
        Set<PermissionItemResponse> permissions = permissionRepository.getPermissionsByRoleId(roleId).stream()
                .map(permission -> new PermissionItemResponse(
                        permission.getId(), permission.getResourceId(), permission.getAction(), permission.getPermissionCode()))
                .collect(Collectors.toSet());
        return new PermissionResponse(roleId, permissions);
    }

    @Transactional
    @CacheEvict(value = {HAS_PERMISSION, ROLE_PERMISSIONS}, allEntries = true)
    public void updateRolePermission(Long roleId, RolePermissionRequest request) {
        AppRole appRole = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        processPermissionExchange(permissionMapper.toEntity(request.permissions(), appRole), appRole);
    }

    @Transactional
    @CacheEvict(value = {HAS_PERMISSION, CacheConfig.CacheNames.USER_PERMISSIONS}, allEntries = true)
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

        createPermissionChangeRecord(currentPermissions, permissionsIncoming, permissible);
    }

    private void createPermissionChangeRecord(Set<AppPermission> oldValue,
                                              Set<AppPermission> newValue,
                                              Permissible permissible) {

        PermissionChange permissionChange = PermissionChange.builder()
                .appRole(permissible instanceof AppRole ? (AppRole) permissible : null)
                .appUser(permissible instanceof AppUser ? (AppUser) permissible : null)
                .oldValue(new PermissionChange.PermissionChangeRecord(permissionMapper.toDto(oldValue)))
                .newValue(new PermissionChange.PermissionChangeRecord(permissionMapper.toDto(newValue)))
                .build();
        permissionChangeRepository.save(permissionChange);
    }
}
