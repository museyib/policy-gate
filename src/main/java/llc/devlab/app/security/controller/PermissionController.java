package llc.devlab.app.security.controller;

import llc.devlab.app.security.model.dto.PermissionResponse;
import llc.devlab.app.security.model.dto.RolePermissionRequest;
import llc.devlab.app.security.model.dto.UserPermissionRequest;
import llc.devlab.app.security.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
public class PermissionController {
    private final PermissionService permissionService;

    @GetMapping("/role/{roleId}")
    @PreAuthorize("hasPermission('PERMISSION', 'READ')")
    public ResponseEntity<PermissionResponse> getRolePermission(@PathVariable Long roleId) {
        PermissionResponse permissionResponse = permissionService.getRolePermission(roleId);
        return ResponseEntity.ok(permissionResponse);
    }

    @PostMapping("/role/{roleId}")
    @PreAuthorize("hasPermission('PERMISSION', 'UPDATE')")
    public ResponseEntity<Void> updateRolePermission(@PathVariable Long roleId, @RequestBody RolePermissionRequest request) {
        permissionService.updateRolePermission(roleId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasPermission('PERMISSION', 'READ')")
    public ResponseEntity<PermissionResponse> getUserPermission(@PathVariable Long userId) {
        PermissionResponse permissionResponse = permissionService.getUserPermission(userId);
        return ResponseEntity.ok(permissionResponse);
    }

    @PostMapping("/user/{userId}")
    @PreAuthorize("hasPermission('PERMISSION', 'UPDATE')")
    public ResponseEntity<Void> updateUserPermission(@PathVariable Long userId, @RequestBody UserPermissionRequest request) {
        permissionService.updateUserPermission(userId, request);
        return ResponseEntity.ok().build();
    }
}
