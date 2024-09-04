package com.example.ToDolist.security;

import static java.util.stream.Collectors.joining;

import com.example.ToDolist.model.User;
import com.example.ToDolist.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
public class JwtTokenProvider {
    private final SecurityConstants securityConstants;
    private final UserRepository userRepository;
    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        var secret = Base64.getEncoder().encodeToString(securityConstants.SECRET_KEY.getBytes());
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(Authentication authentication) {
        String username = authentication.getName();

        User user = userRepository.findByUsername(username);

        Claims claims = Jwts.claims().setSubject(username).setId(user.getId().toString());

        Date now = new Date();
        int expireDelay = Integer.parseInt(securityConstants.EXPIRATION_TIME);
        Date validity = new Date(now.getTime() + expireDelay * 1000L);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(this.secretKey, SignatureAlgorithm.HS256)
                .compact();

    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(this.secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        try {
            User principal = new User()
                    .setId(Long.parseLong(claims.getId()))
                    .setUsername(claims.getSubject());

            return new UsernamePasswordAuthenticationToken(principal, token, null);

        } catch (NumberFormatException e) {
            return null;
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(this.secretKey).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}