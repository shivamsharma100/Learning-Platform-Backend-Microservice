package com.example.course.auth;

import com.example.course.entities.Role;
import com.example.course.entities.User;
import com.example.course.repositories.RoleRepository;
import com.example.course.repositories.UserRepository;
import com.example.course.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private Controller controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ==========================
    // Test for /login endpoint
    // ==========================
    @Test
    void testLogin_Success() {
        // Arrange
        Controller.AuthRequest request = new Controller.AuthRequest();
        request.setUsername("john");
        request.setPassword("pass");

        User mockUser = User.builder()
                .username("john")
                .roles(Set.of(new Role(1L,"ADMIN")))
                .build();

        when(userRepository.findByUsername("john")).thenReturn(Optional.of(mockUser));
        when(jwtUtil.generateToken(eq("john"), anySet())).thenReturn("dummy-token");

        // Act
        ResponseEntity<?> response = controller.login(request);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Controller.AuthResponse authResponse = (Controller.AuthResponse) response.getBody();
        assertNotNull(authResponse);
        assertEquals("dummy-token", authResponse.getToken());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository).findByUsername("john");
        verify(jwtUtil).generateToken(eq("john"), anySet());
    }

    // ==========================
    // Test for /register endpoint
    // ==========================
    @Test
    void testRegister_Success() {
        // Arrange
        Role role = new Role(1L,"ADMIN");

        Controller.RegisterRequest request = new Controller.RegisterRequest();
        request.setUsername("alice");
        request.setPassword("password123");
        request.setRoles(Set.of(role));

        when(roleRepository.findByName(role.getName())).thenReturn(Optional.of(role));
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");

        // Act
        ResponseEntity<?> response = controller.register(request);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User registered successfully", response.getBody());

        // Verify userRepository.save called with correct user
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertEquals("alice", savedUser.getUsername());
        assertEquals("encodedPassword", savedUser.getPassword());
        assertEquals(1, savedUser.getRoles().size());
        assertTrue(savedUser.getRoles().contains(role));

        verify(roleRepository).findByName("ADMIN");
        verify(passwordEncoder).encode("password123");
    }

    // ==========================
    // Test for register role not found
    // ==========================
    @Test
    void testRegister_RoleNotFound() {
        Role role = new Role(1L,"ADMIN");
        Controller.RegisterRequest request = new Controller.RegisterRequest();
        request.setUsername("bob");
        request.setPassword("pwd");
        request.setRoles(Set.of(role));

        when(roleRepository.findByName("ADMIN")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> controller.register(request));

        assertTrue(exception.getMessage().contains("Role not found"));
        verify(userRepository, never()).save(any());
    }
}
