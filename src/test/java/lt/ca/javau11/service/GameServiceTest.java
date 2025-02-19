package lt.ca.javau11.service;

import lt.ca.javau11.model.Game;
import lt.ca.javau11.repository.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {
    @InjectMocks
    private GameService gameService;

    @Mock
    private GameRepository gameRepository;

    @BeforeEach
    void setUp() {
        // No need to call MockitoAnnotations.openMocks(this) with @ExtendWith(MockitoExtension.class)
    }

    @Test
    void findAll() {
        Game game = new Game("Test Game", "Description", "PC", LocalDate.now(), "/images/test.jpg");
        when(gameRepository.findAll()).thenReturn(List.of(game));
        List<Game> games = gameService.findAll();
        assertNotNull(games);
        assertEquals(1, games.size());
        assertEquals("Test Game", games.get(0).getTitle());
    }

    @Test
    void findById_Found() {
        Game game = new Game("Test Game", "Description", "PC", LocalDate.now(), "/images/test.jpg");
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        Optional<Game> foundGame = gameService.findById(1L);
        assertTrue(foundGame.isPresent());
        assertEquals("Test Game", foundGame.get().getTitle());
    }

    @Test
    void findById_NotFound() {
        when(gameRepository.findById(1L)).thenReturn(Optional.empty());
        Optional<Game> foundGame = gameService.findById(1L);
        assertFalse(foundGame.isPresent());
    }

    @Test
    void save_Success() throws IOException {
        Game game = new Game("Test Game", "Description", "PC", LocalDate.now(), "/images/test.jpg");
        MultipartFile file = new MockMultipartFile("test.jpg", new byte[0]);
        when(gameRepository.save(any(Game.class))).thenReturn(game);
        Game savedGame = gameService.save(game, file);
        assertNotNull(savedGame);
        assertEquals("Test Game", savedGame.getTitle());
        verify(gameRepository, times(1)).save(any(Game.class));
    }

    @Test
    void save_ExceptionHandling() throws IOException {
        Game game = new Game("Test Game", "Description", "PC", LocalDate.now(), "/images/test.jpg");
        MultipartFile file = new MockMultipartFile("test.jpg", new byte[0]);
        doThrow(new RuntimeException("File upload failed")).when(gameRepository).save(any(Game.class));
        assertThrows(RuntimeException.class, () -> gameService.save(game, file));
        verify(gameRepository, times(1)).save(any(Game.class));
    }

    @Test
    void save_InvalidFile() {
        Game game = new Game("Test Game", "Description", "PC", LocalDate.now(), "/images/test.jpg");
        MultipartFile file = null; // Assuming null file is invalid
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> gameService.save(game, file));
        assertEquals("File cannot be null", exception.getMessage());
    }
}