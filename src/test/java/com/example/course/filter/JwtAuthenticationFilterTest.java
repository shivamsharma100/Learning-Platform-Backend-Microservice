package com.example.course.filter;

import com.example.course.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtAuthenticationFilterTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private StringWriter responseWriter;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.clearContext();
        responseWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));
    }

    @Test
    void testDoFilterInternal_withValidTokenAndAllowedRole() throws Exception {
        String token = "validToken";
        String username = "user1";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.getUsernameFromToken(token)).thenReturn(username);
        when(jwtUtil.validateToken(token)).thenReturn(true);
        when(jwtUtil.getRolesFromToken(token)).thenReturn(Set.of("ADMIN"));

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Security context should be populated
        var auth = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(auth);
        assertEquals(username, auth.getName());
        // Filter chain should continue
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_withValidTokenButDisallowedRole() throws Exception {
        String token = "validToken";
        String username = "user1";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.getUsernameFromToken(token)).thenReturn(username);
        when(jwtUtil.validateToken(token)).thenReturn(true);
        when(jwtUtil.getRolesFromToken(token)).thenReturn(Set.of("ROLE1")); // Not allowed

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Response should be forbidden
        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
        // Filter chain should NOT continue
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_withInvalidToken() throws Exception {
        String token = "invalidToken";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.getUsernameFromToken(token)).thenThrow(new RuntimeException("Invalid JWT"));

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Security context should be empty
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        // Filter chain should continue
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_withNoAuthorizationHeader() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Security context should remain empty
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        // Filter chain should continue
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_withAuthorizationHeaderWithoutBearer() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Token xyz");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Security context should remain empty
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        // Filter chain should continue
        verify(filterChain).doFilter(request, response);
    }
}
