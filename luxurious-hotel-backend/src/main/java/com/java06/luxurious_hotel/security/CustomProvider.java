package com.java06.luxurious_hotel.security;

import com.java06.luxurious_hotel.dto.AuthorityDTO;
import com.java06.luxurious_hotel.entity.UserEntity;
import com.java06.luxurious_hotel.exception.user.IncorrectPasswordException;
import com.java06.luxurious_hotel.exception.user.UserNotFoundException;
import com.java06.luxurious_hotel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomProvider implements AuthenticationProvider {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(()-> new UserNotFoundException());

        if (!passwordEncoder.matches(password, userEntity.getPassword())){
            throw new IncorrectPasswordException();
        }

//        AuthorityDTO authorityDTO = new AuthorityDTO();
//        authorityDTO.setId(userEntity.getId());
//        authorityDTO.setEmail(userEntity.getEmail());
//        authorityDTO.setFirstName(userEntity.getFirstName());
//        authorityDTO.setLastName(userEntity.getLastName());

        List<GrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority(userEntity.getRole().getName()+" "+userEntity.getEmail()+" "+"http://localhost:9999/file/"+userEntity.getImage()));
        return new UsernamePasswordAuthenticationToken("", "", authorityList);
    }

    @Override
    public boolean supports(Class<?> authentication) {// xác định loại authen là user name password
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
