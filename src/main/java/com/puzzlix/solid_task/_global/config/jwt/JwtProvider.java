package com.puzzlix.solid_task._global.config.jwt;

import com.puzzlix.solid_task.domain.user.Role;
import com.puzzlix.solid_task.domain.user.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class JwtProvider {

    private final SecretKey key;
    private final long validityInMilliseconds;

    public JwtProvider(@Value("${jwt.secret}") String key, @Value("${jwt.expiration-in-ms}") long validityInMilliseconds) {
        this.key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(key));
        this.validityInMilliseconds = validityInMilliseconds;
    }

    public String createToken(User user) {
        Date now  = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("role", user.getRole().name())
                .issuedAt(now)
                .expiration(validity)
                .signWith(key)
                .compact();
    }

    public String getSubject(String token) {
        return parseClaims(token).getSubject();
    }

    public Role getRole(String token) {
        return Role.valueOf(parseClaims(token).get("role").toString());
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build().parseSignedClaims(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("잘못된 JWT 입니다.");
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT 입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT 입니다.");
        } catch (Exception e) {
            log.error("JWT 인증에 문제가 생겼습니다.");
        }
        return false;
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
