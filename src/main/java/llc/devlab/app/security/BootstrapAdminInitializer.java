package llc.devlab.app.security;

import llc.devlab.app.config.BootstrapAdminProperties;
import llc.devlab.app.security.model.entity.AppUser;
import llc.devlab.app.security.model.enums.UserType;
import llc.devlab.app.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class BootstrapAdminInitializer implements ApplicationRunner {

    private final BootstrapAdminProperties bootstrapAdminProperties;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(@NonNull ApplicationArguments args) {
        if (!StringUtils.hasText(bootstrapAdminProperties.email()) || !StringUtils.hasText(bootstrapAdminProperties.password())) {
            return;
        }

        if (userRepository.existsByEmailIgnoreCase(bootstrapAdminProperties.email())) {
            return;
        }

        userRepository.save(AppUser.builder()
                .email(bootstrapAdminProperties.email().trim().toLowerCase())
                .displayName(bootstrapAdminProperties.displayName())
                .passwordHash(passwordEncoder.encode(bootstrapAdminProperties.password()))
                .userType(UserType.ADMIN)
                .rootUser(true)
                .build());
    }
}
