package com.example.springsecurityjwtdemo.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;

@Component
public class AppJwtUtils {
    @Value("${APP_SECRET}") //  SpEL (from environment variable)
    private String SERVER_SECRET;

    public byte[] getSigningKey() {
        return SERVER_SECRET.getBytes(StandardCharsets.UTF_8);
    }

    public String extractUsername(String token) {
        return extractClaim(token, claimsJws -> claimsJws.getBody().getSubject());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, (claimsJws -> claimsJws.getBody().getExpiration()));
    }

    public <T> T extractClaim(String token, Function<Jws<Claims>, T> claimsResolver) {
        final Jws<Claims> claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Jws<Claims> extractAllClaims(String token)  {
        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(this.getSigningKey()))
                .build()
                .parseClaimsJws(token);

        return claims;
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        List<String> roles = new ArrayList<>();

        userDetails.getAuthorities().forEach(a -> {
            roles.add(a.getAuthority());
        });

        claims.put("ROLE", roles);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plus(30, ChronoUnit.MINUTES)))
                .signWith(Keys.hmacShaKeyFor(this.getSigningKey()))
                .compact();
    }

    public boolean validateToken(String token, UserDetails userDetails) throws io.jsonwebtoken.ExpiredJwtException, io.jsonwebtoken.UnsupportedJwtException, io.jsonwebtoken.MalformedJwtException, io.jsonwebtoken.security.SignatureException, IllegalArgumentException {
        final String username = extractUsername(token);
        return (
                username.equals(userDetails.getUsername())
                        && userDetails.isEnabled()
                        && !isTokenExpired(token));
    }
}
