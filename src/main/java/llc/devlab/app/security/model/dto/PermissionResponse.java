package llc.devlab.app.security.model.dto;

import java.util.Set;

public record PermissionResponse(Long id, Set<PermissionItem> permissions) {
}
