package llc.devlab.app.security.model.entity;

import jakarta.persistence.*;
import llc.devlab.app.model.entity.AuditableEntity;
import llc.devlab.app.security.model.enums.Action;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "resource")
public class Resource extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    private String name;
}