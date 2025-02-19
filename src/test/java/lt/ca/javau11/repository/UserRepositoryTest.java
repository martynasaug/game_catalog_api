package lt.ca.javau11.repository;

import lt.ca.javau11.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByUsername_Found() {
        User user = new User("testuser", "password", Set.of("ROLE_USER"));
        userRepository.save(user);

        Optional<User> foundUser = userRepository.findByUsername("testuser");

        assertTrue(foundUser.isPresent());
        assertEquals("testuser", foundUser.get().getUsername());
    }

    @Test
    void findByUsername_NotFound() {
        Optional<User> foundUser = userRepository.findByUsername("testuser");

        assertFalse(foundUser.isPresent());
    }
}