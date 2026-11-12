package llc.devlab.app.security.repository;

import llc.devlab.app.security.model.entity.PermissionChange;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionChangeRepository extends JpaRepository<PermissionChange, Long> {
}
