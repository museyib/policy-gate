package llc.devlab.app.security.model.entity;

import jakarta.persistence.*;
import llc.devlab.app.model.entity.AuditableEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "app_role")
public class AppRole extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "appRole")
    private Set<AppPermission> permissions;
}