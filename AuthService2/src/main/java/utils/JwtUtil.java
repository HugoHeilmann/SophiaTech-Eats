package utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class JwtUtil {
    private static Set<String> invalidatedTokens = new HashSet<>();

    // Secret key to sign the JWT
    private static final String SECRET_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9eyJzdWIiOiJ1c2VyQGVtYWlsLmNvbSIsImlhdCI6MTYwMjA0MjMyMn0SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJVadQssw5c";

    // JWT expiration time (e.g., 1 hour)
    private static final long EXPIRATION_TIME = 3600000;

    // Method to create JWT token
    public static String createToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date()) // Current time
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Set expiration time
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY) // Sign with the secret key
                .compact();
    }

    public static boolean validateToken(String token) {
        if (invalidatedTokens.contains(token)) {
            return false;
        }

        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getEmailFromToken(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
    }

    public static void invalidateToken(String token) {
        invalidatedTokens.add(token);
    }
}
