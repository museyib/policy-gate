package llc.devlab.app.security.repository;
import llc.devlab.app.security.model.dto.PermissionItem;
import llc.devlab.app.security.model.entity.AppPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface PermissionRepository extends JpaRepository<AppPermission, Long> {
    @Query("""
            SELECT p.id as id,
                   p.resource.id as resourceId,
                   p.action as action,
                   concat(p.resource.code, ':', p.action) as permissionCode
            FROM AppPermission p
            WHERE p.appUser.id = :userId
                OR p.appRole.id IN (SELECT r.id FROM AppUser u JOIN u.roles r WHERE u.id = :userId)
            ORDER BY p.id""")
    Set<PermissionItem> getPermissionsByUserId(@Param("userId") Long userId);

    @Query("""
            SELECT p.id as id,
                   p.resource.id as resourceId,
                   p.action as action,
                   concat(p.resource.code, ':', p.action) as permissionCode
            FROM AppPermission p
            WHERE p.appRole.id = :roleId
            ORDER BY p.id""")
    Set<PermissionItem> getPermissionsByRoleId(@Param("roleId") Long roleId);
}
