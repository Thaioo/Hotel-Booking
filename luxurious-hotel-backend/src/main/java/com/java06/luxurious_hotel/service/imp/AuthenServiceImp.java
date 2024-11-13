package com.java06.luxurious_hotel.service.imp;

import com.java06.luxurious_hotel.dto.AuthorityDTO;
import com.java06.luxurious_hotel.entity.InvalidTokenEntity;
import com.java06.luxurious_hotel.exception.authen.TokenInvalidException;
import com.java06.luxurious_hotel.repository.InvalidTokenRepository;
import com.java06.luxurious_hotel.request.AuthenRequest;
import com.java06.luxurious_hotel.service.AuthenService;
import com.java06.luxurious_hotel.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class AuthenServiceImp implements AuthenService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private InvalidTokenRepository invalidTokenRepository;

    @Override
    public String login(AuthenRequest request) {

        UsernamePasswordAuthenticationToken authenToken =
                new UsernamePasswordAuthenticationToken(request.username(), request.password());
        Authentication authen = authenticationManager.authenticate(authenToken);

        String roleAndEmailAndImage = authen.getAuthorities()
                .stream().map(grantedAuthority -> grantedAuthority.getAuthority().toString()
                ).findFirst().get();

        String role = roleAndEmailAndImage.split(" ")[0];
        String email = roleAndEmailAndImage.split(" ")[1];
        String image = roleAndEmailAndImage.split(" ")[2];

        AuthorityDTO authorityDTO = new AuthorityDTO();
        authorityDTO.setUsername(request.username());
        authorityDTO.setRole(role);
        authorityDTO.setEmail(email);
        authorityDTO.setImage(image);



        System.out.println("Username: " + request.username()+ " Role: " + role+" Email: " + email+" Image: " + image);



        return jwtUtils.generateJwtToken(authorityDTO);
    }

    @Override
    public boolean logout(String headerToken) {

        String token = jwtUtils.getTokenFromHeader(headerToken);
        if (token == null) return false;

        Jws<Claims> claims = jwtUtils.getClaims(token);
        InvalidTokenEntity invalidTokenEntity = new InvalidTokenEntity();
        invalidTokenEntity.setToken(token);
//        invalidTokenEntity.setExpTime(claims.getPayload().getExpiration().toInstant()
//                .atZone(ZoneId.of("UTC"))
//                .toLocalDateTime());
        invalidTokenEntity.setExpTime(LocalDateTime.now());

        invalidTokenRepository.save(invalidTokenEntity);

        return true;
    }
}
