package llc.devlab.app.security.repository;

import llc.devlab.app.security.model.entity.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<AppRole, Long> {
}
