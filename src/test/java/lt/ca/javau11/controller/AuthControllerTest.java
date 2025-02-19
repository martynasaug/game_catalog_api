package lt.ca.javau11.controller;

import lt.ca.javau11.model.User;
import lt.ca.javau11.service.UserService;
import lt.ca.javau11.security.jwt.JwtUtils;
import lt.ca.javau11.service.CustomUserDetailsService.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserService userService;

    @Mock
    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_Success() {
        User user = new User("testuser", "password", Set.of("ROLE_USER"));
        when(userService.findByUsername("testuser")).thenReturn(Optional.empty());
        ResponseEntity<String> response = authController.registerUser(user);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("User registered successfully!", response.getBody());
        verify(userService, times(1)).register(user);
    }

    @Test
    void registerUser_UsernameTaken() {
        User user = new User("testuser", "password", Set.of("ROLE_USER"));
        when(userService.findByUsername("testuser")).thenReturn(Optional.of(user));
        ResponseEntity<String> response = authController.registerUser(user);
        assertEquals(400, response.getStatusCode().value());
        assertEquals("Username is already taken!", response.getBody());
        verify(userService, never()).register(user);
    }

    @Test
    void authenticateUser_Success() {
        // Create a mock CustomUserDetails object
        CustomUserDetails customUserDetails = mock(CustomUserDetails.class);
        when(customUserDetails.getUsername()).thenReturn("testuser");
        when(customUserDetails.getPassword()).thenReturn("password");
        when(customUserDetails.getAuthorities()).thenReturn(Set.of(new SimpleGrantedAuthority("ROLE_USER")));

        // Create a mock Authentication object
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(customUserDetails); // Set the principal to the CustomUserDetails object

        // Configure the authentication manager to return the mock Authentication object
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);

        // Mock JWT token generation
        when(jwtUtils.generateToken(any())).thenReturn("jwtToken");

        // Call the controller method
        ResponseEntity<Map<String, Object>> response = authController.authenticateUser(new User("testuser", "password", Set.of("ROLE_USER")));

        // Assertions
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("jwtToken", response.getBody().get("token"));

        // Verify interactions
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void authenticateUser_Failure() {
        User user = new User("testuser", "password", Set.of("ROLE_USER"));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Authentication failed"));
        ResponseEntity<Map<String, Object>> response = authController.authenticateUser(user);
        assertEquals(500, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Login failed: Authentication failed", response.getBody().get("error"));
    }
}