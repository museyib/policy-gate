package llc.devlab.app.security.model.dto;

import java.util.Set;

public record RolePermissionRequest(Set<PermissionRequestItem> permissions) {
}
