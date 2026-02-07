package com.Krishnendu.BillingSoftware.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTUtils {

    @Value("${jwt.secret.key}")
    private String SECRET_KEY;

    public String generateJWTToken(UserDetails userDetails) {

        Map<String, Object> claims = new HashMap<String, Object>();
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return
                Jwts.builder()
                        .setClaims(claims)
                        .setSubject(subject)
                        .setIssuedAt(new Date(System.currentTimeMillis()))
                        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 2)) //Valid up to 2 hours
                        .signWith(getKeyFromSecretKey(SECRET_KEY))
                        .compact();

    }

    private Key getKeyFromSecretKey(String secretKey) {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public Claims getClaimsFromToken(String token) {
        return
                Jwts.parserBuilder()
                        .setSigningKey(getKeyFromSecretKey(SECRET_KEY))
                        .build()
                        .parseClaimsJws(token)
                        .getBody();
    }

    public String getUsernameFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    public Date getExpirationTimeFromToken(String token) {
        return getClaimsFromToken(token).getExpiration();
    }

    public boolean isValidToken(String token, UserDetails userDetails) {
        try {
            Claims claims = getClaimsFromToken(token);

            String subject = claims.getSubject();

            if (!userDetails.getUsername().equals(subject) && !isTokenExpired(token)) {
                return false;
            }
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }

        return true;
    }
    public boolean isTokenExpired(String token) {
        return getClaimsFromToken(token).getExpiration().before(new Date());
    }

}
