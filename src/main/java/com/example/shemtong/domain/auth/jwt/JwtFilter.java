package com.example.shemtong.domain.auth.jwt;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    private final List<String> permitAllPaths = Arrays.asList(
            "/auth/**","/swagger-ui/**", "/v3/api-docs/**"
    );

    private boolean isAuthenticatedPath(String path) {
        return permitAllPaths.stream().anyMatch(path::startsWith);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        if (isAuthenticatedPath(path)) {
            filterChain.doFilter(request, response);
            log.info("Jwt Filter1");
            return;
        }

        log.info("Jwt Filter2");
        String authorizationHeader = request.getHeader("Authorization");


        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            log.info("Jwt Filter3");
            String token = authorizationHeader.substring(7);

            if (!isAccessToken(token)) {
                log.info("Jwt Filter4");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            log.info("Jwt Filter5");

            jwtUtil.isTokenValid(token);

            Authentication auth = jwtUtil.getAuthenticationByToken(token);
            SecurityContextHolder.getContext().setAuthentication(auth);

        }
        log.info("filter path: {}", path);
        filterChain.doFilter(request, response);
    }

    private boolean isAccessToken(String token) {

        Claims claims = jwtUtil.extractAllClaims(token);
        String tokenType = claims.get("token_type", String.class);
        return "access".equals(tokenType);  // 'access'라는 타입일 때만 인증 허용
    }
}
