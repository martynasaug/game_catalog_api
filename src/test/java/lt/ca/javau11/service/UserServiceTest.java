package lt.ca.javau11.service;

import lt.ca.javau11.model.User;
import lt.ca.javau11.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findByUsername_Found() {
        User user = new User("testuser", "password", Set.of("ROLE_USER"));
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.findByUsername("testuser");

        assertTrue(foundUser.isPresent());
        assertEquals("testuser", foundUser.get().getUsername());
    }

    @Test
    void findByUsername_NotFound() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        Optional<User> foundUser = userService.findByUsername("testuser");

        assertFalse(foundUser.isPresent());
    }

    @Test
    void register_Success() {
        User user = new User("testuser", "password", Set.of("ROLE_USER"));
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(user);

        User registeredUser = userService.register(user);

        assertNotNull(registeredUser);
        assertEquals("encodedPassword", registeredUser.getPassword());
    }

    @Test
    void isCurrentUserAdmin_True() {
        User user = new User("testuser", "password", Set.of("ROLE_ADMIN"));
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        boolean isAdmin = userService.isCurrentUserAdmin("testuser");

        assertTrue(isAdmin);
    }

    @Test
    void isCurrentUserAdmin_False() {
        User user = new User("testuser", "password", Set.of("ROLE_USER"));
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        boolean isAdmin = userService.isCurrentUserAdmin("testuser");

        assertFalse(isAdmin);
    }
}