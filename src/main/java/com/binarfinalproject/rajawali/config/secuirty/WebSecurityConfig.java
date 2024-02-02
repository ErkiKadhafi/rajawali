package com.binarfinalproject.rajawali.config.secuirty;

import com.binarfinalproject.rajawali.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {
  @Autowired
  JWTAuthenticationFilter jwtAuthenticationFilter;

  @Autowired
  AuthenticationProvider authenticationProvider;

  @Autowired
  AuthEntryPointJwt unauthorizedHandler;

  @Autowired
  UserRepository userRepository;

  // @Bean
  // AccessDeniedHandler customAccessDeniedHandler() {
  //   return (request, response, accessDeniedException) -> {
  //     // custom Handling here
  //   };
  // }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable())
        .exceptionHandling(configurer -> configurer.authenticationEntryPoint(unauthorizedHandler))
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authenticationProvider(authenticationProvider)
        .authorizeHttpRequests(auth -> auth
            .anyRequest()
            .permitAll())
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }
}
