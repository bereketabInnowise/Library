package library.controller;

import library.dto.RegisterRequestDTO;
import library.model.Role;
import library.model.User;
import library.repository.RoleRepository;
import library.repository.UserRepository;
import library.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthController authController;

    private RegisterRequestDTO request;

    @BeforeEach
    void setUp() {
        request = new RegisterRequestDTO();
        request.setUsername("testuser");
        request.setPassword("testpass");
    }

    @Test
    void register_valid() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("testpass")).thenReturn("encodedPass");
        when(userRepository.save(any(User.class))).thenReturn(new User());
        when(roleRepository.save(any(Role.class))).thenReturn(new Role());

        ResponseEntity<String> response = authController.register(request);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("User registered successfully", response.getBody());
        verify(userRepository).save(any(User.class));
        verify(roleRepository).save(any(Role.class));
    }

    @Test
    void register_usernameExists() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(new User()));
        ResponseEntity<String> response = authController.register(request);
        assertEquals(400, response.getStatusCode().value());
        assertEquals("Username already exists", response.getBody());
        verify(userRepository, never()).save(any(User.class));
    }
}