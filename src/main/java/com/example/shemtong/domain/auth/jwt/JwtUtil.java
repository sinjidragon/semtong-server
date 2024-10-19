package com.example.shemtong.domain.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    private long expirationMs = 3600000;
    private long refreshExpirationMs = 604800000;

    public String generateToken(String id) {
        return Jwts.builder()
                .setSubject(id)
                .claim("token_type", "access")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public String generateRefreshToken(String id) {
        return Jwts.builder()
                .setSubject(id)
                .claim("token_type", "refresh")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpirationMs))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public Long extractUserId(String token) {
        return Long.parseLong(extractAllClaims(token).getSubject());
    }


    public boolean isTokenValid(String token) {
        return !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        Date expiration = extractAllClaims(token).getExpiration();
        return expiration != null && expiration.before(new Date());
    }


    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    public Authentication getAuthenticationByToken(String token) {
        Claims claims = extractAllClaims(token);

        User principal = new User(claims.getSubject(), "",
                Collections.singleton(new SimpleGrantedAuthority("MEMBER")));

        return new UsernamePasswordAuthenticationToken(principal, token,
                Collections.singleton(new SimpleGrantedAuthority("MEMBER")));
    }
}
