package llc.devlab.app.security.model.dto;

public interface PermissionItem {
    Long getId();
    Long getResourceId();
    String getAction();
    String getPermissionCode();
}
