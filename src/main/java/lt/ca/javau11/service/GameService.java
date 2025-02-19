package lt.ca.javau11.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import lt.ca.javau11.model.Game;
import lt.ca.javau11.repository.GameRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class GameService {
    @Autowired
    private GameRepository gameRepository;

    // Find all games - Parameterized as List<Game>
    public List<Game> findAll() {
        return gameRepository.findAll();
    }

    // Find a game by ID - Parameterized as Optional<Game>
    public Optional<Game> findById(Long id) {
        return gameRepository.findById(id);
    }

    // Save a new game or update an existing one
    public Game save(Game game, MultipartFile file) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("File cannot be null");
        }
        if (!file.isEmpty()) {
            byte[] bytes = file.getBytes();
            Path path = Paths.get("src/main/resources/static/images/" + file.getOriginalFilename());
            Files.write(path, bytes);
            game.setImageUrl("/images/" + file.getOriginalFilename());
        }
        return gameRepository.save(game);
    }

    // Update an existing game
    public Game update(Game game, MultipartFile file) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("File cannot be null");
        }
        if (!file.isEmpty()) {
            byte[] bytes = file.getBytes();
            Path path = Paths.get("src/main/resources/static/images/" + file.getOriginalFilename());
            Files.write(path, bytes);
            game.setImageUrl("/images/" + file.getOriginalFilename());
        }
        return gameRepository.save(game);
    }

    // Delete a game by ID
    public void delete(Long id) {
        gameRepository.deleteById(id);
    }
}