package library.controller;

import library.Application;
import library.dto.LoginRequestDTO;
import library.dto.RegisterRequestDTO;
import library.model.Role;
import library.model.User;
import library.repository.RoleRepository;
import library.repository.UserRepository;
//import library.security.TestSecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {Application.class}, properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
@Testcontainers
class AuthControllerIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("library")
            .withUsername("postgres")
            .withPassword("password");

    @Autowired private MockMvc mockMvc;
    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {
        roleRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void register_success() throws Exception {
        String body = "{\"username\": \"newuser\", \"password\": \"newpass\"}";
        mockMvc.perform(post("/api/v1/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("User registered successfully"));
    }
    @Test
    void login_success() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword(passwordEncoder.encode("testpass"));
        user.setEnabled(true);
        userRepository.save(user);
        Role role = new Role();
        role.setUser(user);
        role.setRole("ROLE_USER");
        roleRepository.save(role);

        String requestBody = "{\"username\": \"testuser\", \"password\": \"testpass\"}";
        mockMvc.perform(post("/api/v1/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(header().exists("Authorization"))
                .andExpect(jsonPath("$").value("Login successful"));
    }
}