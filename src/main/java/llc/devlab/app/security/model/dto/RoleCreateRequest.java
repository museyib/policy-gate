package llc.devlab.app.security.model.dto;

import jakarta.validation.constraints.NotBlank;

public record RoleCreateRequest(@NotBlank String name) {
}
