package llc.devlab.app.security;

import llc.devlab.app.security.model.entity.AppUser;
import lombok.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public record AccountPrincipal(Long id, String email, String displayName, String passwordHash,
                               String userType) implements UserDetails {

    public static AccountPrincipal from(AppUser appUser) {
        return new AccountPrincipal(
                appUser.getId(),
                appUser.getEmail(),
                appUser.getDisplayName(),
                appUser.getPasswordHash(),
                appUser.getUserType().name()
        );
    }

    @Override
    @NonNull
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + userType));
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    @NonNull
    public String getUsername() {
        return email;
    }
}
