package llc.devlab.app.security.service;

import llc.devlab.app.exception.ResourceNotFoundException;
import llc.devlab.app.security.mapper.PermissionMapper;
import llc.devlab.app.security.model.dto.*;
import llc.devlab.app.security.model.entity.AppPermission;
import llc.devlab.app.security.model.entity.AppRole;
import llc.devlab.app.security.model.entity.AppUser;
import llc.devlab.app.security.repository.PermissionRepository;
import llc.devlab.app.security.repository.RoleRepository;
import llc.devlab.app.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class PermissionService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final PermissionMapper rolePermissionMapper;

    public PermissionDto getUserPermission(Long id) {
        Set<PermissionItem> permissions = permissionRepository.getPermissionsByUserId(id);
        return new PermissionDto(id, permissions);
    }

    public PermissionDto getRolePermission(Long id) {
        Set<PermissionItem> permissions = permissionRepository.getPermissionsByRoleId(id);
        return new PermissionDto(id, permissions);
    }

    @Transactional
    public void updateRolePermission(Long roleId, RolePermissionRequest request) {
        AppRole appRole = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        Set<AppPermission> appPermissions = rolePermissionMapper.toEntity(roleId, request);
        permissionRepository.deleteAll(appRole.getPermissions());
        permissionRepository.saveAll(appPermissions);
    }

    @Transactional
    public void updateUserPermission(Long userId, UserPermissionRequest request) {
        AppUser appUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Set<AppPermission> appPermissions = rolePermissionMapper.toEntity(userId, request);
        permissionRepository.deleteAll(appUser.getPermissions());
        permissionRepository.saveAll(appPermissions);
    }

}
