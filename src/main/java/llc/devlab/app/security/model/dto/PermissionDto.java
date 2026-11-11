package llc.devlab.app.security.model.dto;

import java.util.Set;

public record PermissionDto(Long id, Set<PermissionItem> permissions) {
}
