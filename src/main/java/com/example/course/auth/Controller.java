package com.example.course.auth;

import com.example.course.entities.User;
import com.example.course.entities.Role;
import com.example.course.repositories.RoleRepository;
import com.example.course.repositories.UserRepository;
import com.example.course.util.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class Controller {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = userRepository.findByUsername(request.getUsername()).get();
        Set<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());

        String token = jwtUtil.generateToken(user.getUsername(), roles);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        Set<Role> userRoles = request.getRoles().stream()
                .map(role -> roleRepository.findByName(role.getName())
                        .orElseThrow(() -> new RuntimeException("Role not found: " + role)))
                .collect(Collectors.toSet());

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(userRoles)
                .build();

        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully");
    }


    @Data
    static class AuthRequest {
        private String username;
        private String password;
    }

    @Data
    @AllArgsConstructor
    static class AuthResponse {
        private String token;
    }

    @Data
    static class RegisterRequest {
        private String username;
        private String password;
        private Set<Role> roles; // ADMIN, INSTRUCTOR, LEARNER
    }
}
