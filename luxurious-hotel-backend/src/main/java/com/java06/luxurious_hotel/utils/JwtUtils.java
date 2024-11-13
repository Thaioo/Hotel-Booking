package com.java06.luxurious_hotel.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.java06.luxurious_hotel.dto.AuthorityDTO;
import com.java06.luxurious_hotel.exception.authen.TokenInvalidException;
import com.java06.luxurious_hotel.repository.InvalidTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtils {

    @Autowired
    private InvalidTokenRepository invalidTokenRepository;

    @Value("${jwt.key}")
    private String secretKeyString;

    private long EXPIRATION_TIME = 365L * 24 * 60 * 60 * 1000; //1 năm
//    private long EXPIRATION_TIME = 60 * 1000; //1 p

    private long EXPIRATION_TIME_1P = 60 * 1000; // 1 phút

    private long CONFIRM_EXP_TIME = 15*60*1000; //15 phút confirm booking

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

    public String generateJwtTokenResetPassword(AuthorityDTO authorityDTO) {
        var secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKeyString));
        Date now = new Date();
        //System.out.println(authorityDTO.getUsername()+" "+authorityDTO.getEmail()+" "+authorityDTO.getRole()+" "+authorityDTO.getImage());
        return Jwts.builder()
                .subject(authorityDTO.getEmail())
                .issuedAt(new Date())
                .expiration(new Date(now.getTime() + EXPIRATION_TIME_1P))
                .claim("role", authorityDTO.getRole())
                .claim("email", authorityDTO.getEmail())
                .claim("avatar", authorityDTO.getImage())
                .signWith(secretKey)
                .compact();
    }

    public String generateConfirmCreateUser(String email) {
        var secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKeyString));
        Date now = new Date();
        return Jwts.builder()
                .subject(String.valueOf(email))
                .issuedAt(new Date())
                .expiration(new Date(now.getTime() + CONFIRM_EXP_TIME))
                .signWith(secretKey)
                .compact();
    }


    public String verifyConfirmToken(String token) {
        Jws<Claims> claims = this.getClaims(token);
        return claims.getPayload().getSubject();
    }

    public String generateJwtToken(AuthorityDTO authorityDTO) {
        var secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKeyString));
        Date now = new Date();
        //System.out.println(authorityDTO.getUsername()+" "+authorityDTO.getEmail()+" "+authorityDTO.getRole()+" "+authorityDTO.getImage());
        return Jwts.builder()
                .subject(authorityDTO.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(now.getTime() + EXPIRATION_TIME))
                .claim("role", authorityDTO.getRole())
                .claim("email", authorityDTO.getEmail())
                .claim("avatar", authorityDTO.getImage())
                .signWith(secretKey)
                .compact();
    }

    public AuthorityDTO verifyToken(String token) throws JsonProcessingException {

        AuthorityDTO authorityDTO = new AuthorityDTO();

        Jws<Claims> claims = this.getClaims(token);

        //Kiếm tra xem token đã logout hay chưa
        if (invalidTokenRepository.findByToken(token).isPresent()) {
            throw new TokenInvalidException();
        }

        authorityDTO.setUsername(claims.getPayload().getSubject());
        authorityDTO.setRole(claims.getPayload().get("role", String.class));

        return authorityDTO;
    }

    public Jws<Claims> getClaims(String token) {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKeyString));
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
    }

    public String getTokenFromHeader(String bearerToken) {
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
