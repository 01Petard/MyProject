package com.hzx.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    private static final String SECRET = "your_secret";
    private static final Algorithm algorithm;

    static {
        try {
            algorithm = Algorithm.HMAC256(SECRET);
        } catch (Exception e) {
            throw new RuntimeException("Unsupported encoding", e);
        }
    }

    public String generateToken(Long userId, String username) {
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 有效期为10小时
                .withClaim("userId", userId)
                .withClaim("username", username)
                .sign(algorithm);
    }

    public boolean validateToken(String token) {
        try {
            JWT.require(algorithm)
                    .build()
                    .verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        DecodedJWT decodedJWT = JWT.require(algorithm)
                .build()
                .verify(token);
        return decodedJWT.getSubject();
    }

    public Long getUserIdFromToken(String token) {
        DecodedJWT decodedJWT = JWT.require(algorithm)
                .build()
                .verify(token);
        return decodedJWT.getClaim("userId").asLong();
    }

    public Map<String, Claim> getAllClaimsFromToken(String token) {
        DecodedJWT decodedJWT = JWT.require(algorithm)
                .build()
                .verify(token);
        return decodedJWT.getClaims();
    }
}