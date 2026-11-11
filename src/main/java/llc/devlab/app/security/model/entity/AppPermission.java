package llc.devlab.app.security.model.entity;

import jakarta.persistence.*;
import llc.devlab.app.security.model.enums.Action;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "app_permission")
public class AppPermission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Resource resource;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private Action action;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id")
    private AppRole appRole;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private AppUser appUser;
}