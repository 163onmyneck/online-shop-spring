package com.example.demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    private static final Key secret = Keys.hmacShaKeyFor(
                        "asdasfserdsfvxvsdfgdgdfgqwew2134324dfgdfgdafdsfsdfs"
                            .getBytes(StandardCharsets.UTF_8));
    private static final long expiration = 300000L;

    public String generateToken(String userEmail) {
        return Jwts.builder()
            .setSubject(userEmail)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(secret)
            .compact();
    }

    public boolean isValid(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                                        .setSigningKey(secret)
                                        .build()
                                        .parseClaimsJws(token);

            return claimsJws.getBody().getExpiration().after(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtException("Expired or invalid JWT token");
        }
    }

    public String getUsername(String token) {
        return getClaimsFromToken(token, Claims::getSubject);
    }

    private <T> T getClaimsFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parserBuilder()
                                .setSigningKey(secret)
                                .build()
                                .parseClaimsJws(token)
                                .getBody();
        return claimsResolver.apply(claims);
    }
}
