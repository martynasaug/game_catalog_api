package lt.ca.javau11.controller;

import lt.ca.javau11.model.Game;
import lt.ca.javau11.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
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
        when(gameService.findAll(null)).thenReturn(List.of(game));

        ResponseEntity<List<Game>> response = gameController.getAllGames(null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Test Game", response.getBody().get(0).getTitle());
    }

    @Test
    void getGameById_Found() {
        Game game = new Game("Test Game", "Description", "PC", LocalDate.now(), "/images/test.jpg");
        when(gameService.findById(1L)).thenReturn(Optional.of(game));

        ResponseEntity<Game> response = gameController.getGameById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test Game", response.getBody().getTitle());
    }

    @Test
    void getGameById_NotFound() {
        when(gameService.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Game> response = gameController.getGameById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getFeaturedGames() {
        Game game1 = new Game("Game 1", "Description", "PC", LocalDate.now(), "/images/game1.jpg");
        Game game2 = new Game("Game 2", "Description", "PC", LocalDate.now(), "/images/game2.jpg");
        when(gameService.findAll()).thenReturn(List.of(game1, game2));

        ResponseEntity<List<Game>> response = gameController.getFeaturedGames();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void createGame_Success() throws Exception {
        MultipartFile file = mock(MultipartFile.class);
        Game game = new Game("Test Game", "Description", "PC", LocalDate.now(), "/images/test.jpg");
        when(gameService.save(any(Game.class), eq(file))).thenReturn(game);

        ResponseEntity<Game> response = gameController.createGame(file, "Test Game", "Description", "PC", "2023-01-01");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test Game", response.getBody().getTitle());
    }

    @Test
    void updateGame_Success() throws Exception {
        MultipartFile file = mock(MultipartFile.class);
        Game existingGame = new Game("Old Game", "Old Desc", "PC", LocalDate.now(), "/images/old.jpg");
        Game updatedGame = new Game("New Game", "New Desc", "PC", LocalDate.now(), "/images/new.jpg");
        when(gameService.findById(1L)).thenReturn(Optional.of(existingGame));
        when(gameService.update(any(Game.class), eq(file))).thenReturn(updatedGame);

        ResponseEntity<Game> response = gameController.updateGame(1L, file, "New Game", "New Desc", "PC", "2023-01-01");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("New Game", response.getBody().getTitle());
    }

    @Test
    void updateGame_NotFound() throws Exception {
        MultipartFile file = mock(MultipartFile.class);
        when(gameService.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Game> response = gameController.updateGame(1L, file, "New Game", "New Desc", "PC", "2023-01-01");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteGame_Success() {
        Game game = new Game("Test Game", "Description", "PC", LocalDate.now(), "/images/test.jpg");
        when(gameService.findById(1L)).thenReturn(Optional.of(game));
        doNothing().when(gameService).delete(1L);

        ResponseEntity<Void> response = gameController.deleteGame(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deleteGame_NotFound() {
        when(gameService.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Void> response = gameController.deleteGame(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
