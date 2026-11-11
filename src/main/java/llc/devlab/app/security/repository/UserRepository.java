package llc.devlab.app.security.repository;

import llc.devlab.app.security.model.entity.AppUser;
import lombok.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {

    @EntityGraph(attributePaths = {"roles", "permissions"})
    Optional<AppUser> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);

    @EntityGraph(attributePaths = {"roles", "permissions"})
    @NonNull
    List<AppUser> findAll();

    @EntityGraph(attributePaths = {"roles", "permissions"})
    @NonNull
    Optional<AppUser> findById(@NonNull Long id);
}
