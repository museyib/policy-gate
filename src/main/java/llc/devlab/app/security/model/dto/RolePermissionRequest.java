package llc.devlab.app.security.model.dto;

import llc.devlab.app.security.model.enums.Action;

import java.util.List;

public record RolePermissionRequest(List<Permission> permissions) {
    public record Permission(Long resourceId,
                             Action action) {
    }
}
