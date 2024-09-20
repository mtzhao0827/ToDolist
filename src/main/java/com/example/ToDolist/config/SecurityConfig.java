package com.example.ToDolist.config;

import com.example.ToDolist.exception.basic.unauthorized.UnauthorizedException;
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
import org.springframework.jdbc.core.metadata.CallParameterMetaData;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.security.core.userdetails.UserDetails;

import java.lang.reflect.Array;

@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
@AllArgsConstructor
@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain springWebFilterChain(
            HttpSecurity http,
            JwtTokenProvider tokenProvider
    ) throws Exception {

        return http.httpBasic( AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(c ->
                        c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeRequests(c -> c
                        .requestMatchers(HttpMethod.GET,"/**.html").permitAll()
                        .requestMatchers(HttpMethod.GET,"/**.js").permitAll()
                        .requestMatchers(HttpMethod.GET,"/**.css").permitAll()
                        .requestMatchers(HttpMethod.GET,"/").permitAll()
                        .requestMatchers(HttpMethod.POST,"/v1/users/register").permitAll()
                        .requestMatchers(HttpMethod.POST,"/v1/users/login").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(
                        new JwtTokenAuthenticationFilter(tokenProvider),
                        UsernamePasswordAuthenticationFilter.class
                )
                .build();
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
    public AuthenticationManager customAuthenticationManager(UserDetailsService userDetailsService,
                                                             PasswordEncoder encoder) {
        return authentication -> {
            String username = authentication.getPrincipal() + "";
            String password = authentication.getCredentials() + "";
            UserDetails user = userDetailsService.loadUserByUsername(username);
            if (!encoder.matches(password, user.getPassword())) {
                throw new UnauthorizedException("密码错误");
            }
            if (!user.isEnabled()) {
                throw new DisabledException("账户未启用");
            }
            return new UsernamePasswordAuthenticationToken(username, null, user.getAuthorities());
        };
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
