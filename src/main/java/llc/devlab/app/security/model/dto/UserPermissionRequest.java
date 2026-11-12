package llc.devlab.app.security.model.dto;

import java.util.Set;

public record UserPermissionRequest(Set<PermissionRequestItem> permissions) {
}
