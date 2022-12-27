package itacad.aliaksandrkryvapust.auditmicroservice.controller.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil {
    public static final long JWT_TOKEN_VALID_TIME = 1 * 60 * 60; // 1 hour
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String getUsername(String token) {
        return this.getClaim(token, Claims::getSubject);
    }

    public Date getExpirationDate(String token) {
        return this.getClaim(token, Claims::getExpiration);
    }

    public <TYPE> TYPE getClaim(String token, Function<Claims, TYPE> clamsResolver) {
        final Claims claims = getAllClaims(token);
        return clamsResolver.apply(claims);
    }

    private Claims getAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder().setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALID_TIME * 1000))
                .signWith(key).compact();
    }

    public boolean validate(String token, UserDetails userDetails) {
        final String username = getUsername(token);
        return username.equals(userDetails.getUsername()) && !this.isTokenExpired(token);
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDate(token);
        return expiration.before(new Date());
    }
}
