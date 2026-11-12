package llc.devlab.app.security.model.entity;

import java.util.Set;

public interface Permissible {
    Long getId();
    Set<AppPermission> getPermissions();
}
