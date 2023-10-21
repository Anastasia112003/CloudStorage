package com.example.cloudstorage.security;

import com.example.cloudstorage.config.AuthenticationConstant;
import com.example.cloudstorage.model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class JWTToken {
    private final SecretKey secret;
    private final int tokenLifetime;
    private final List<String> listTokens = new ArrayList<>();
    private User user;

    public JWTToken(@Value("${jwt.secret}") String secret, @Value("${TOKEN_LIFETIME}") int tokenLifetime) {
        this.secret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.tokenLifetime = tokenLifetime;
    }

    public User getAuthenticatedUser() {
        return user;
    }

    public String generateToken(@NonNull User user) throws IllegalArgumentException {
        this.user = user;
        Date now = new Date();
        Date exp = Date.from(LocalDateTime.now().plusMinutes(tokenLifetime)
                .atZone(ZoneId.systemDefault()).toInstant());

        String token = Jwts.builder()
                .setId(String.valueOf(user.getId()))
                .setSubject(user.getLogin())
                .setIssuedAt(now)
                .setNotBefore(now)
                .setExpiration(exp)
                .signWith(secret)
                .claim("roles", user.getRoles())
                .compact();
        listTokens.add(token);
        return token;
    }

    public boolean validateAccessToken(@NonNull String token) {
        for (String t : listTokens) {
            if (!t.equals(token)) {
                return false;
            }
        }
        return validateToken(token, secret);
    }

    private boolean validateToken(@NonNull String token, @NonNull Key secret) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token);
            return true;

        } catch (ExpiredJwtException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedJwtException e) {
            throw new RuntimeException(e);
        } catch (MalformedJwtException e) {
            throw new RuntimeException(e);
        } catch (SignatureException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    public Claims getAccessClaims(@NonNull String token) {
        return getClaims(token, secret);
    }

    private Claims getClaims(@NonNull String token, @NonNull Key secret) {
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public void removeToken(String token) {
        listTokens.remove(token.substring(AuthenticationConstant.TOKEN_PREFIX.length()));
    }
}
