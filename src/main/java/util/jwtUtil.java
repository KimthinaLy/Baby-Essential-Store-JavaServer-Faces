/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import model.User;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 *
 * @author Admin
 */
public class jwtUtil {
    private static final String JWT_SECRET = "JSFSuperSecretBabyStoreKey2026";
    private static final SecretKey KEY = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));

    public static String generateToken(User user) {
        return Jwts.builder()
                .subject(user.getEmail())
                .claim("role", user.getRole())
                .claim("userId", user.getUserId())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hour
                .signWith(KEY)
                .compact();
    }

    public static Claims validateToken(String token) {
        try {
            return Jwts.parser().verifyWith(KEY)
                    .build().parseSignedClaims(token).getPayload();
        } catch (Exception e) {
            return null;
        }
    }
}
