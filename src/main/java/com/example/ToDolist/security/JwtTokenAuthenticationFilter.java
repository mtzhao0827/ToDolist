package com.example.ToDolist.security;


import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;


@AllArgsConstructor
public class JwtTokenAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
            throws IOException, ServletException {

        String token = resolveToken((HttpServletRequest) req);
        boolean isTokenValid = jwtTokenProvider.validateToken(token);

        if (token != null && isTokenValid) {
            Authentication auth = jwtTokenProvider.getAuthentication(token);

            if (auth != null && !(auth instanceof AnonymousAuthenticationToken)) {
                SecurityContextHolder.getContext().setAuthentication(auth);
            }

        }

        filterChain.doFilter(req, res);
    }

    private String resolveToken(HttpServletRequest request) {

        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }
}
