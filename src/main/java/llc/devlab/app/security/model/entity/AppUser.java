package llc.devlab.app.security.model.entity;

import jakarta.persistence.*;
import llc.devlab.app.model.entity.AuditableEntity;
import llc.devlab.app.security.model.enums.UserType;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "app_user")
public class AppUser extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false, length = 120)
    private String displayName;

    @Enumerated(EnumType.STRING)
    private UserType userType = UserType.USER;

    private boolean rootUser = false;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<AppRole> roles;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "appUser")
    private Set<AppPermission> permissions;
}
