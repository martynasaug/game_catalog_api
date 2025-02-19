package lt.ca.javau11.controller;

import lt.ca.javau11.model.Game;
import lt.ca.javau11.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameControllerTest {

    @InjectMocks
    private GameController gameController;

    @Mock
    private GameService gameService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllGames() {
        Game game = new Game("Test Game", "Description", "PC", LocalDate.now(), "/images/test.jpg");
        when(gameService.findAll()).thenReturn(List.of(game));

        ResponseEntity<List<Game>> response = gameController.getAllGames();

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode()); // Use HttpStatusCode instead of integer
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Test Game", response.getBody().get(0).getTitle());
    }

    @Test
    void getGameById_Found() {
        Game game = new Game("Test Game", "Description", "PC", LocalDate.now(), "/images/test.jpg");
        when(gameService.findById(1L)).thenReturn(Optional.of(game));

        ResponseEntity<Game> response = gameController.getGameById(1L);

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode()); // Use HttpStatusCode instead of integer
        assertNotNull(response.getBody());
        assertEquals("Test Game", response.getBody().getTitle());
    }

    @Test
    void getGameById_NotFound() {
        when(gameService.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Game> response = gameController.getGameById(1L);

        assertEquals(HttpStatusCode.valueOf(404), response.getStatusCode()); // Use HttpStatusCode instead of integer
    }

    @Test
    void createGame_Success() throws Exception {
        MultipartFile file = mock(MultipartFile.class);
        Game game = new Game("Test Game", "Description", "PC", LocalDate.now(), "/images/test.jpg");
        when(gameService.save(any(Game.class), eq(file))).thenReturn(game);

        ResponseEntity<Game> response = gameController.createGame(file, "Test Game", "Description", "PC", "2023-01-01");

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode()); // Use HttpStatusCode instead of integer
        assertNotNull(response.getBody());
        assertEquals("Test Game", response.getBody().getTitle());
    }
}