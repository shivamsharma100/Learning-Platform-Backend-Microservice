package com.example.course.config;

import com.example.course.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.example.course.AppConstants.*;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CSRF is disabled because this is a stateless REST API using JWT for authentication
        http
                // CSRF is enabled by default but ignored for stateless REST API endpoints
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/**") // ignore CSRF only for /api endpoints
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/swagger-ui/index.html"
                        ).permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/courses").hasAnyRole(ADMIN, INSTRUCTOR)
                        .requestMatchers(HttpMethod.GET, "/api/courses/course/*").hasAnyRole(INSTRUCTOR, LEARNER, ADMIN)
                        .requestMatchers(HttpMethod.GET, "/api/courses").hasAnyRole(INSTRUCTOR, LEARNER, ADMIN)
                        .requestMatchers(HttpMethod.PUT, "/api/courses/*").hasAnyRole(ADMIN, INSTRUCTOR)
                        .requestMatchers(HttpMethod.GET, "/api/enrollment/courses/*/enrollments").hasAnyRole(LEARNER, ADMIN)
                        .requestMatchers(HttpMethod.POST, "/api/enrollment/courses/*/enrollments").hasRole(ADMIN)
                        .requestMatchers("/api/lesson/courses/*/lessons").hasRole(INSTRUCTOR)
                        .requestMatchers("/api/lesson/courses/*/lessons").hasAnyRole(INSTRUCTOR, LEARNER, ADMIN)
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean()
    @Primary
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}