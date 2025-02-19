package lt.ca.javau11.repository;

import lt.ca.javau11.model.Game;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class GameRepositoryTest {

    @Autowired
    private GameRepository gameRepository;

    @Test
    void findAll() {
        Game game = new Game("Test Game", "Description", "PC", LocalDate.now(), "/images/test.jpg");
        gameRepository.save(game);

        List<Game> games = gameRepository.findAll();

        assertNotNull(games);
        assertEquals(1, games.size());
        assertEquals("Test Game", games.get(0).getTitle());
    }

    @Test
    void findById_Found() {
        Game game = new Game("Test Game", "Description", "PC", LocalDate.now(), "/images/test.jpg");
        gameRepository.save(game);

        Optional<Game> foundGame = gameRepository.findById(game.getId());

        assertTrue(foundGame.isPresent());
        assertEquals("Test Game", foundGame.get().getTitle());
    }

    @Test
    void findById_NotFound() {
        Optional<Game> foundGame = gameRepository.findById(1L);

        assertFalse(foundGame.isPresent());
    }
}