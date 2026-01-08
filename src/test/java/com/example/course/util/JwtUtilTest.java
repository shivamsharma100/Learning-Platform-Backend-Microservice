package com.example.course.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    private static final String SECRET = "test-secret-key-for-jwt";

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(SECRET);
    }

    @Test
    void generateToken_ShouldReturnNonNullToken() {
        String token = jwtUtil.generateToken("john", Set.of("ROLE_USER"));

        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void getUsernameFromToken_ShouldReturnCorrectUsername() {
        String token = jwtUtil.generateToken("john", Set.of("ROLE_USER"));

        String username = jwtUtil.getUsernameFromToken(token);

        assertEquals("john", username);
    }

    @Test
    void getRolesFromToken_ShouldReturnCorrectRoles() {
        Set<String> roles = Set.of("ROLE_USER", "ROLE_ADMIN");
        String token = jwtUtil.generateToken("john", roles);

        Set<String> extractedRoles = jwtUtil.getRolesFromToken(token);

        assertEquals(roles, extractedRoles);
    }

    @Test
    void validateToken_ShouldReturnTrueForValidToken() {
        String token = jwtUtil.generateToken("john", Set.of("ROLE_USER"));

        boolean isValid = jwtUtil.validateToken(token);

        assertTrue(isValid);
    }

    @Test
    void validateToken_ShouldReturnFalseForInvalidToken() {
        boolean isValid = jwtUtil.validateToken("invalid.jwt.token");

        assertFalse(isValid);
    }

    @Test
    void validateToken_ShouldReturnFalseForTokenSignedWithDifferentSecret() {
        String token = jwtUtil.generateToken("john", Set.of("ROLE_USER"));

        JwtUtil anotherJwtUtil = new JwtUtil("another-secret");

        boolean isValid = anotherJwtUtil.validateToken(token);

        assertFalse(isValid);
    }
}
