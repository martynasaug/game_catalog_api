package lt.ca.javau11.repository;

import lt.ca.javau11.model.Game;
import lt.ca.javau11.model.Review;
import lt.ca.javau11.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameRepository gameRepository;

    @Test
    void findByGameId() {
        User user = new User("testuser", "password", Set.of("ROLE_USER"));
        userRepository.save(user);

        Game game = new Game("Test Game", "Description", "PC", LocalDate.now(), "/images/test.jpg");
        gameRepository.save(game);

        Review review = new Review("Great game!", 5, user, game);
        reviewRepository.save(review);

        List<Review> reviews = reviewRepository.findByGameId(game.getId());

        assertNotNull(reviews);
        assertEquals(1, reviews.size());
        assertEquals("Great game!", reviews.get(0).getComment());
    }

    @Test
    void findUserById() {
        User user = new User("testuser", "password", Set.of("ROLE_USER"));
        userRepository.save(user);

        Optional<User> foundUser = reviewRepository.findUserById(user.getId());

        assertTrue(foundUser.isPresent());
        assertEquals("testuser", foundUser.get().getUsername());
    }

    @Test
    void findGameById() {
        Game game = new Game("Test Game", "Description", "PC", LocalDate.now(), "/images/test.jpg");
        gameRepository.save(game);

        Optional<Game> foundGame = reviewRepository.findGameById(game.getId());

        assertTrue(foundGame.isPresent());
        assertEquals("Test Game", foundGame.get().getTitle());
    }
}