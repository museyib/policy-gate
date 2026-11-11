package llc.devlab.app.security.model.dto;

import llc.devlab.app.security.model.enums.UserType;

import java.util.Set;

public record UserDto(
        Long id,
        String email,
        String displayName,
        UserType userType,
        Set<RoleDto> roles) {
}
