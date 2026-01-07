package com.example.course.config;

import com.example.course.filter.JwtAuthenticationFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SecurityConfigUnitTest {

    @Mock
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private org.springframework.security.core.userdetails.UserDetailsService userDetailsService;

    private SecurityConfig securityConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        securityConfig = new SecurityConfig(jwtAuthenticationFilter, userDetailsService);
    }

    @Test
    void testPasswordEncoderBean() {
        PasswordEncoder encoder = securityConfig.passwordEncoder();
        assertNotNull(encoder);
        // BCryptPasswordEncoder is the actual implementation
        assertEquals(org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.class, encoder.getClass());
    }

    @Test
    void testSecurityFilterChainBean_creation() throws Exception {
        HttpSecurity httpSecurity = mock(HttpSecurity.class, RETURNS_DEEP_STUBS);

        // Mock methods to allow building
        when(httpSecurity.csrf(any())).thenReturn(httpSecurity);
        when(httpSecurity.sessionManagement(any())).thenReturn(httpSecurity);
        when(httpSecurity.authorizeHttpRequests(any())).thenReturn(httpSecurity);
        when(httpSecurity.addFilterBefore(any(), any())).thenReturn(httpSecurity);

        SecurityFilterChain chain = securityConfig.securityFilterChain(httpSecurity);
        assertNotNull(chain);

        // Verify that addFilterBefore was called with our JWT filter
        verify(httpSecurity).addFilterBefore(eq(jwtAuthenticationFilter), eq(org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class));
    }
}
