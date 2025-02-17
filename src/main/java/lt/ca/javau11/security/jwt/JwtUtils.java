package lt.ca.javau11.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lt.ca.javau11.service.CustomUserDetailsService.CustomUserDetails;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    private String jwtSecret = "eb256418cc2b4499acad276cf0fe7d4c2ce4394770c87e6d613fe74d3535c993";
    private int jwtExpirationMs = 86400000; // 1 day

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("id", ((CustomUserDetails) userDetails).getId()) // Include userId
                .claim("roles", ((CustomUserDetails) userDetails).getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList()) // Include roles
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public Long getUserIdFromJwtToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        return (Long) claims.get("id");
    }

    public List<String> getRolesFromJwtToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        Object rolesObject = claims.get("roles");
        if (rolesObject instanceof List) {
            return ((List<?>) rolesObject).stream()
                    .filter(role -> role instanceof String)
                    .map(Object::toString)
                    .toList();
        }
        return List.of(); // Return an empty list if roles are not present or invalid
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (Exception e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        }
        return false;
    }
}