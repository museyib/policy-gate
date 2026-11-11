package llc.devlab.app.security.repository;

import llc.devlab.app.security.model.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceRepository extends JpaRepository<Resource, Long> {
}
