package com.java06.luxurious_hotel.security;

import com.java06.luxurious_hotel.filter.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.net.http.HttpRequest;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, CustomProvider provider) throws Exception {

        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(provider).build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,
           JwtTokenFilter jwtTokenFilter, CorsConfigurationSource corsConfigurationSource) throws Exception {
        httpSecurity
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(request -> request

                        .requestMatchers("/authen/**").permitAll()
                                .requestMatchers("/role/**").permitAll()
                                .requestMatchers("/signup").permitAll()
                                .requestMatchers("/signup/confirm").permitAll()
                                .requestMatchers("/roomType/file/**").permitAll()// hình ảnh
                                .requestMatchers("/file/**").permitAll()// hình ảnh
                                .requestMatchers(HttpMethod.GET,"/room").permitAll()// search Avai Room
                                .requestMatchers(HttpMethod.POST,"/room").permitAll()// search Avai Room
                                .requestMatchers(HttpMethod.GET,"/roomType/detail/**").permitAll()// sear avai detail
                                .requestMatchers(HttpMethod.GET,"/booking/p**").permitAll()
                                .requestMatchers(HttpMethod.POST,"/booking").permitAll()
                                .requestMatchers(HttpMethod.POST,"/booking/confirm").permitAll()
                                .requestMatchers("/email/**").permitAll()
                                .requestMatchers("/status/**").permitAll()
                                .requestMatchers("/user/p**").permitAll()


                                .requestMatchers("/roomType/**").hasAnyAuthority("ROLE_ADMIN","ROLE_HOTEL_MANAGER")
                                .requestMatchers("/booking/**").hasAnyAuthority("ROLE_ADMIN","ROLE_HOTEL_MANAGER")
//                                .requestMatchers("/status/**").hasAnyAuthority("ROLE_ADMIN","ROLE_HOTEL_MANAGER")

                                .requestMatchers("/employee/**").hasAuthority("ROLE_ADMIN")
                                .requestMatchers("/user/**").hasAuthority("ROLE_ADMIN")
                                .requestMatchers("/room/**").hasAnyAuthority("ROLE_ADMIN","ROLE_HOTEL_MANAGER")

                                .requestMatchers("/ws/**").permitAll()
                                .requestMatchers("notifications/**").hasAnyAuthority("ROLE_ADMIN","ROLE_HOTEL_MANAGER")
                        .anyRequest().authenticated()
                        //.requestMatchers("/**").permitAll()
                )
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                // nếu khi Author bị vấn đề thì httpBasic sẽ gọi đến EntryPoint để báo lỗi 401
                .httpBasic(basic-> basic.authenticationEntryPoint(authenticationEntryPoint))
                .exceptionHandling(Customizer.withDefaults());

        return httpSecurity.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        //configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));  // This allows all origins with credentials

        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
