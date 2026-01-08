package com.example.course.service;

import com.example.course.entities.Role;
import com.example.course.entities.User;
import com.example.course.exception.UserNotFoundException;
import com.example.course.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService userDetailsService;

    @Test
    void testLoadUserByUsername_UserExists() {
        // Prepare test data
        Role role = new Role();
        role.setName("ADMIN");
        Set<Role> set = new HashSet<>();
        set.add(role);

        User user = new User();
        user.setUsername("john");
        user.setPassword("password123");
        user.setRoles(set);

        // Mock repository
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));

        // Call the service
        UserDetails userDetails = userDetailsService.loadUserByUsername("john");

        // Verify results
        assertNotNull(userDetails);
        assertEquals("john", userDetails.getUsername());
        assertEquals("password123", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));

        // Verify repository interaction
        verify(userRepository, times(1)).findByUsername("john");
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        // Mock repository to return empty
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        // Verify exception is thrown
        assertThrows(UserNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("unknown"));

        // Verify repository interaction
        verify(userRepository, times(1)).findByUsername("unknown");
    }
}
