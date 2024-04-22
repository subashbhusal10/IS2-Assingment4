package com.hidemessage.security;


import com.hidemessage.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.TemporalUnit;
import java.util.Date;

import static io.jsonwebtoken.security.Keys.hmacShaKeyFor;

@Service
public class JwtService {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    public Claims validateToken(String jwt) {
        return Jwts.
                parserBuilder().
                setSigningKey(jwtSecret.getBytes(StandardCharsets.UTF_8))
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }

    public String createToken(User user, Long expiryTime, TemporalUnit timeUnit) {
        Date tokenExpiry = Date.from(Instant.now().plus(expiryTime, timeUnit));
        Key secretKey = hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder().
                setSubject(user.getEmail()).
                setExpiration(tokenExpiry)
                .signWith(secretKey)
                .compact();
    }
}
