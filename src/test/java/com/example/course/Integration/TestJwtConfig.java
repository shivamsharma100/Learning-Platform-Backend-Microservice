package com.example.course.Integration;

import com.example.course.util.JwtUtil;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.Set;

import static org.mockito.Mockito.when;

@TestConfiguration
class TestJwtConfig {

    @Bean
    JwtUtil jwtUtil() {
        JwtUtil jwtUtil = Mockito.mock(JwtUtil.class);

        when(jwtUtil.validateToken("admin-token")).thenReturn(true);
        when(jwtUtil.getUsernameFromToken("admin-token")).thenReturn("admin");
        when(jwtUtil.getRolesFromToken("admin-token"))
                .thenReturn(Set.of("ADMIN"));

        when(jwtUtil.validateToken("instructor-token")).thenReturn(true);
        when(jwtUtil.getUsernameFromToken("instructor-token")).thenReturn("instructor");
        when(jwtUtil.getRolesFromToken("instructor-token"))
                .thenReturn(Set.of("INSTRUCTOR"));

        when(jwtUtil.validateToken("learner-token")).thenReturn(true);
        when(jwtUtil.getUsernameFromToken("learner-token")).thenReturn("learner");
        when(jwtUtil.getRolesFromToken("learner-token"))
                .thenReturn(Set.of("LEARNER"));

        return jwtUtil;
    }
}