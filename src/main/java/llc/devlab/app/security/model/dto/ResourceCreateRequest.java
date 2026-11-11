package llc.devlab.app.security.model.dto;

import jakarta.validation.constraints.NotBlank;

public record ResourceCreateRequest(@NotBlank String code,
                                    String name) {
}
