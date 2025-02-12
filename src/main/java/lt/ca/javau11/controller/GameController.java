package lt.ca.javau11.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import lt.ca.javau11.model.Game;
import lt.ca.javau11.service.GameService;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/games")
public class GameController {

    private static final String RELEASE_DATE = "releaseDate";
    private static final String PLATFORM2 = "platform";
    private static final String DESCRIPTION2 = "description";
    private static final String TITLE2 = "title";

    @Autowired
    private GameService gameService;

    @GetMapping
    public ResponseEntity<List<Game>> getAllGames() {
        return ResponseEntity.ok(gameService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Game> getGameById(@PathVariable Long id) {
        return gameService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<Game> createGame(
            @RequestParam(required = false) MultipartFile file,
            @RequestParam(TITLE2) String title,
            @RequestParam(DESCRIPTION2) String description,
            @RequestParam(PLATFORM2) String platform,
            @RequestParam(RELEASE_DATE) String releaseDate) throws Exception {

        Game game = new Game();
        game.setTitle(title);
        game.setDescription(description);
        game.setPlatform(platform);
        game.setReleaseDate(LocalDate.parse(releaseDate));
        Game savedGame = gameService.save(game, file);
        return ResponseEntity.ok(savedGame);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Game> updateGame(
            @PathVariable Long id,
            @RequestParam(required = false) MultipartFile file,
            @RequestParam(TITLE2) String title,
            @RequestParam(DESCRIPTION2) String description,
            @RequestParam(PLATFORM2) String platform,
            @RequestParam(RELEASE_DATE) String releaseDate) throws Exception {

        Optional<Game> optionalGame = gameService.findById(id);
        if (!optionalGame.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Game existingGame = optionalGame.get();
        existingGame.setTitle(title);
        existingGame.setDescription(description);
        existingGame.setPlatform(platform);
        existingGame.setReleaseDate(LocalDate.parse(releaseDate));
        try {
            Game updatedGame = gameService.update(existingGame, file);
            return ResponseEntity.ok(updatedGame);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable Long id) {
        if (!gameService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        gameService.delete(id);
        return ResponseEntity.noContent().build();
    }
}