package llc.devlab.app.security.model.dto;

import llc.devlab.app.security.model.enums.Action;

import java.util.Objects;

public record PermissionRequestItem(Long resourceId,
                                    Action action) {
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PermissionRequestItem that = (PermissionRequestItem) o;
        return action == that.action && Objects.equals(resourceId, that.resourceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resourceId, action);
    }
}
