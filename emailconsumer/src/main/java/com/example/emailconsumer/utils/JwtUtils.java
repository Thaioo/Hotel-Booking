package com.example.emailconsumer.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtils {
    @Value("${jwt.key}")
    private String secretKeyString;

    private long CONFIRM_EXP_TIME = 24L*60*60*1000; //15 phút confirm booking

    public String generateConfirmBookingToken(int idBooking) {
        var secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKeyString));
        Date now = new Date();
        return Jwts.builder()
                .subject(String.valueOf(idBooking))
                .issuedAt(new Date())
                .expiration(new Date(now.getTime() + CONFIRM_EXP_TIME))
                .signWith(secretKey)
                .compact();
    }


    public String verifyConfirmToken(String token) {
        Jws<Claims> claims = this.getClaims(token);
        return claims.getPayload().getSubject();
    }

    public Jws<Claims> getClaims(String token) {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKeyString));
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
    }

}
