package com.TiagoSoftware.MyLibrary.Services;

import io.jsonwebtoken.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import static java.util.Collections.emptyList;
import java.util.Date;

@Service
@Data
public class JwtService { //LOGIN FILTER
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expirationDateInMs}")
    private int jwtExpirationInMs;

    private static final String PREFIX = "Bearer";

    public JwtService() {}

    public String generateToken(String subject/*, Optional<Map<String, Object>> claims*/) {
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
    public void AddToken(HttpServletResponse res, String subject) {
        res.addHeader("Authorization", PREFIX + " " + this.generateToken(subject));
        res.addHeader("Access-Control-Expose-Headers", "Authorization");
    }

    public Authentication getAuthentication(HttpServletResponse request) {
        String token = request.getHeader("Authorization");
        if(token != null) {
            String subject = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token.replace(PREFIX, ""))
                    .getBody()
                    .getSubject();
            if(subject != null) {
                return new UsernamePasswordAuthenticationToken(
                        subject,
                        null,
                        emptyList()
                );
            }
        }
        return null;
    }

    public boolean validate(String authToken) throws ExpiredJwtException {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
            return false;
        } catch (ExpiredJwtException ex) {
            return false;
        }
    }

    public String getUserFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }
}
