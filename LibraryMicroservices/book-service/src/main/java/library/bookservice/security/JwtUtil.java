package library.bookservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Component
public class JwtUtil {

    @Value("${spring.jwt.secret}")
    private String secret;

    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public String getUsernameFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    // This method extracts roles directly from the token
    public List<SimpleGrantedAuthority> getRolesFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        Object rolesObject = claims.get("roles");

        if (rolesObject instanceof List) {
            @SuppressWarnings("unchecked")
            List<Object> rolesList = (List<Object>) rolesObject;

            return rolesList.stream()
                    .map(roleItem -> {
                        if (roleItem instanceof Map) {
                            // If it's a map (e.g., {"authority": "ROLE_USER"}), extract the "authority" value
                            @SuppressWarnings("unchecked")
                            Map<String, String> roleMap = (Map<String, String>) roleItem;
                            return new SimpleGrantedAuthority(roleMap.get("authority"));
                        } else if (roleItem instanceof String string) {
                            // If it's already a string, use it directly
                            return new SimpleGrantedAuthority(string);
                        }
                        return null; // Handle unexpected types or throw an error
                    })
                    .filter(java.util.Objects::nonNull) // Filter out any nulls from unexpected types
                    .toList();
        }
        return List.of(); // Return empty list if roles are not a list
    }

    private boolean isTokenExpired(String token) {
        return this.getAllClaimsFromToken(token).getExpiration().before(new Date());
    }

    // This is the simplified validation method that only checks the token's signature and expiration
    public boolean validateToken(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            // Token is invalid for any reason (e.g., malformed, bad signature)
            return false;
        }
    }
}