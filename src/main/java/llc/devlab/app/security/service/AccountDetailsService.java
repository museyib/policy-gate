package llc.devlab.app.security.service;

import llc.devlab.app.security.repository.UserRepository;
import llc.devlab.app.security.AccountPrincipal;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @NonNull
    public UserDetails loadUserByUsername(@NonNull String username) {
        return userRepository.findByEmailIgnoreCase(username)
                .map(AccountPrincipal::from)
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));
    }
}
