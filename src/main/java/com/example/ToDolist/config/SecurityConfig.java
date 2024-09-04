package com.example.ToDolist.config;

import com.example.ToDolist.exception.user.UserNotFoundException;
import com.example.ToDolist.model.User;
import com.example.ToDolist.repository.UserRepository;
import com.example.ToDolist.security.RestAuthenticationEntryPoint;
import com.example.ToDolist.security.JwtTokenAuthenticationFilter;
import com.example.ToDolist.security.JwtTokenProvider;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
@AllArgsConstructor
@Component
public class SecurityConfig {

    @Bean
    SecurityFilterChain springWebFilterChain(
            HttpSecurity http,
            JwtTokenProvider tokenProvider
    ) throws Exception {

        return http.httpBasic( AbstractHttpConfigurer::disable)
                .sessionManagement(c ->
                        c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeRequests(c -> c
                        .requestMatchers(HttpMethod.POST,"/users/register").permitAll()
                        .requestMatchers(HttpMethod.POST,"/users/login").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(
                        new JwtTokenAuthenticationFilter(tokenProvider),
                        UsernamePasswordAuthenticationFilter.class
                )
                .build();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE");
            }
        };
    }

    @Bean
     public UserDetailsService customUserDetailsService(UserRepository userRepository) {
        return username -> {
            User user = userRepository.findByUsername(username);

            if (user == null) {
                throw new UserNotFoundException(username);
            }

            return user;
        };
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
