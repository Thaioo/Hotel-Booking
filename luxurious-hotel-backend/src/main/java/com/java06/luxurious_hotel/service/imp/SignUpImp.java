package com.java06.luxurious_hotel.service.imp;

import com.java06.luxurious_hotel.entity.RoleEntity;
import com.java06.luxurious_hotel.entity.UserEntity;
import com.java06.luxurious_hotel.exception.authen.TokenInvalidException;
import com.java06.luxurious_hotel.repository.RoleRepository;
import com.java06.luxurious_hotel.repository.UserRepository;
import com.java06.luxurious_hotel.request.ConfirmCreateAccount;
import com.java06.luxurious_hotel.request.SignUpRequest;
import com.java06.luxurious_hotel.service.EmailService;
import com.java06.luxurious_hotel.service.SignUpService;
import com.java06.luxurious_hotel.utils.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SignUpImp implements SignUpService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    RoleRepository roleRepository;

    @Override
    public void signUp(SignUpRequest signUpRequest) throws MessagingException {
        RoleEntity staffRole = roleRepository.findByName("ROLE_STAFF");
        UserEntity user = new UserEntity();
        user.setEnabled(false);
        user.setUsername(signUpRequest.username());
        user.setPassword(passwordEncoder.encode(signUpRequest.password()));
        user.setEmail(signUpRequest.email());
        user.setFirstName(signUpRequest.firstname());
        user.setLastName(signUpRequest.lastname());
        user.setPhone(signUpRequest.phone());
        user.setRole(staffRole);
        userRepository.save(user);
        emailService.sendConfirmCreateUser(signUpRequest.email());

    }

    @Override
    public String verifySignUp(ConfirmCreateAccount account) throws MessagingException{
        String status = null;
        try {
            String email = jwtUtils.verifyConfirmToken(account.token());
            if (email != null) {
                UserEntity user = userRepository.findUserEntityByEmail(email);
                if (!user.getEnabled() && user != null) {
                    user.setEnabled(true);
                    userRepository.save(user);
                    status = "OK";
                } else {
                    status = "NOT OK";
                }

            }
        }catch (ExpiredJwtException E){
            status="Invalid token";
        }
        return status;
    }
}