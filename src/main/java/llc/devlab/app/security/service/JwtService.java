package llc.devlab.app.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import llc.devlab.app.security.model.entity.AppUser;
import llc.devlab.app.security.config.JwtProperties;
import llc.devlab.app.security.TokenEnvelope;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    private final JwtProperties jwtProperties;
    private final SecretKey signingKey;

    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.signingKey = Keys.hmacShaKeyFor(jwtProperties.secret().getBytes(StandardCharsets.UTF_8));
    }

    public TokenEnvelope generateAccessToken(AppUser appUser) {
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plusSeconds(jwtProperties.accessTokenExpirationMinutes() * 60);

        String token = Jwts.builder()
                .issuer(jwtProperties.issuer())
                .subject(appUser.getEmail())
                .issuedAt(Date.from(issuedAt))
                .expiration(Date.from(expiresAt))
                .claims(Map.of(
                        "type", "access",
                        "accountId", appUser.getId(),
                        "displayName", appUser.getDisplayName(),
                        "userType", appUser.getUserType().name()
                ))
                .signWith(signingKey)
                .compact();

        return new TokenEnvelope(token, expiresAt);
    }

    public Claims parseAccessToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        if (!"access".equals(claims.get("type", String.class))) {
            throw new IllegalArgumentException("Unsupported token type.");
        }

        return claims;
    }
}
