package llc.devlab.app.security.model.dto;

import java.util.Set;

public record UserUpdateRequest(
        String email,
        String displayName,
        Set<Long> roles) {
}
