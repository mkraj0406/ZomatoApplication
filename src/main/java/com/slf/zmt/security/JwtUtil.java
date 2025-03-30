package com.slf.zmt.security;


import com.slf.zmt.entity.User;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET_KEY="aG93U2VjdXJlSXNUaGlzS2V5MTIzNDU2Nzg5MA==";
    private static final long EXPIRATION_TIME = 86400000;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }


    public String generateToken(String email,String role) {
        return Jwts.builder()
                .setSubject(email)  // Sets the email as the subject of the token
                .claim("role", role) // Store role in token
                .setIssuedAt(new Date())  // Sets the creation timestamp
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))  // Sets expiration time
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)  // Signs token with secret key
                .compact();  // Generates token string
    }

    public String extractEmail(String token) {
        System.out.println("extract email token: "+token);
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (ExpiredJwtException e) {
            System.err.println("Token has expired: " + e.getMessage());
            throw e; // Rethrow if needed
        } catch (JwtException e) {
            System.err.println("Token validation error: " + e.getMessage());
            throw e;
        }
    }

    public String extractRole(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .get("role", String.class); // Extract role from JWT
        } catch (ExpiredJwtException e) {
            System.err.println("Token has expired: " + e.getMessage());
            throw e;
        } catch (JwtException e) {
            System.err.println("Token validation error: " + e.getMessage());
            throw e;
        }
    }


    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.err.println("Token has expired: " + e.getMessage());
        } catch (SignatureException e) {
            System.err.println("Invalid signature: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Invalid token: " + e.getMessage());
        }
        return false;
    }

}
