package com.TiagoSoftware.MyLibrary.Services;

import com.TiagoSoftware.MyLibrary.Models.Responses.ResponseModel;
import io.jsonwebtoken.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;

@Service
@Data
public class JwtService {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expirationDateInMs}")
    private int jwtExpirationInMs;

    public JwtService() {
    }

    public String generateToken(String subject/*, Optional<Map<String, Object>> claims*/) {
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + this.jwtExpirationInMs))
                .signWith(SignatureAlgorithm.HS512, Base64
                        .getEncoder()
                        .encodeToString(this.secret
                                .getBytes())
                        )
                        .compact();
    }
/*
    public ResponseModel validateToken(String authToken) throws ExpiredJwtException {
        try {
            Jwts.parser().setSigningKey().parseClaimsJws(authToken);
            return new ResponseModel("Jwt válido", HttpStatus.OK);
        } catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
            return new ResponseModel(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ExpiredJwtException ex) {
            return new ResponseModel(ex.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }*/
}
