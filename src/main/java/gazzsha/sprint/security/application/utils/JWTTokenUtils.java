package gazzsha.sprint.security.application.utils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JWTTokenUtils {
    final SecretKey secretKey;

    @Value("${jwt.lifetime}")
    Duration jwtLifeTime;

    public JWTTokenUtils(SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    public String generateToken(UserDetails userDetails) {
        Map<String,Object> claims = new HashMap<>();
        List<String> rolesList = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        claims.put("roles",rolesList);
        Instant issueDate = Instant.now();
        Instant expiredDate = issueDate.plus(jwtLifeTime);
    return Jwts.builder()
            .claims(claims)
            .subject(userDetails.getUsername())
            .issuedAt(Date.from(issueDate))
            .expiration(Date.from(expiredDate))
            .signWith(secretKey)
            .compact();
    }

    public String getUsername(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public List<String> getRoles(String token) {
        return getAllClaimsFromToken(token).get("roles",List.class);

    }

    private Claims getAllClaimsFromToken(String token) {
        return  Jwts.parser().verifyWith(secretKey) // 1
                .critical().add("b64").and()                   // 2
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
