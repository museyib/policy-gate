package llc.devlab.app.security.model.dto;

public record PermissionItemResponse(Long id, Long resourceId, String action, String permissionCode)
        implements PermissionItem {
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public Long getResourceId() {
        return resourceId;
    }

    @Override
    public String getAction() {
        return action;
    }

    @Override
    public String getPermissionCode() {
        return permissionCode;
    }
}
