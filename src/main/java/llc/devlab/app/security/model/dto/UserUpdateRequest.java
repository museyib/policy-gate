package llc.devlab.app.security.model.dto;

import llc.devlab.app.security.model.enums.UserType;

import java.util.Set;

public record UserUpdateRequest(
        String email,
        String displayName,
        UserType userType,
        Set<Long> roles) {
}
