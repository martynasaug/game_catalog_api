// src/main/java/lt/ca/javau11/controller/AuthController.java

package lt.ca.javau11.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import lt.ca.javau11.model.User;
import lt.ca.javau11.service.UserService;
import lt.ca.javau11.security.jwt.JwtUtils;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    // Register a new user
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        if (userService.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username is already taken!");
        }
        userService.register(user);
        return ResponseEntity.ok("User registered successfully!");
    }

    // Authenticate a user and generate JWT token
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> authenticateUser(@RequestBody User user) {
        try {
            logger.info("Attempting to authenticate user: {}", user.getUsername());
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateToken((org.springframework.security.core.userdetails.UserDetails) authentication.getPrincipal());

            // Extract user details from authentication
            Map<String, Object> response = Map.of(
                "token", jwt,
                "id", ((lt.ca.javau11.service.CustomUserDetailsService.CustomUserDetails) authentication.getPrincipal()).getId(),
                "username", user.getUsername(),
                "roles", jwtUtils.getRolesFromJwtToken(jwt)
            );

            logger.info("Login successful for user: {}", user.getUsername());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Login failed for user: {}, Error: {}", user.getUsername(), e.getMessage());
            return ResponseEntity.status(500).body(Map.of("error", "Login failed: " + e.getMessage()));
        }
    }
}