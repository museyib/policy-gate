package llc.devlab.app.security.service;

import llc.devlab.app.exception.ConflictException;
import llc.devlab.app.exception.ResourceNotFoundException;
import llc.devlab.app.security.RefreshTokenEnvelope;
import llc.devlab.app.security.TokenEnvelope;
import llc.devlab.app.security.mapper.UserMapper;
import llc.devlab.app.security.model.entity.AppUser;
import llc.devlab.app.security.model.enums.UserType;
import llc.devlab.app.security.repository.UserRepository;
import llc.devlab.app.security.model.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final UserMapper userMapper;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String normalizedEmail = normalizeEmail(request.email());
        if (userRepository.existsByEmailIgnoreCase(normalizedEmail)) {
            throw new ConflictException("A user with that email already exists.");
        }

        AppUser appUser = userRepository.save(AppUser.builder()
                .email(normalizedEmail)
                .displayName(request.displayName().trim())
                .passwordHash(passwordEncoder.encode(request.password()))
                .userType(UserType.USER)
                .build());

        return issueTokens(appUser);
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        String normalizedEmail = normalizeEmail(request.email());
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(normalizedEmail, request.password()));

        AppUser appUser = userRepository.findByEmailIgnoreCase(normalizedEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user was not found."));

        return issueTokens(appUser);
    }

    @Transactional
    public AuthResponse refresh(RefreshTokenRequest request) {
        AppUser appUser = refreshTokenService.rotate(request.refreshToken());
        return issueTokens(appUser);
    }

    @Transactional
    public void logout(LogoutRequest request) {
        refreshTokenService.revoke(request.refreshToken());
    }

    private AuthResponse issueTokens(AppUser appUser) {
        TokenEnvelope accessToken = jwtService.generateAccessToken(appUser);
        RefreshTokenEnvelope refreshToken = refreshTokenService.issue(appUser);
        return new AuthResponse(
                accessToken.token(),
                accessToken.expiresAt(),
                refreshToken.token(),
                refreshToken.expiresAt(),
                userMapper.toDto(appUser)
        );
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
