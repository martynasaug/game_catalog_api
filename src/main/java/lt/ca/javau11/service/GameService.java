package lt.ca.javau11.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import lt.ca.javau11.model.Game;
import lt.ca.javau11.model.Review;
import lt.ca.javau11.repository.GameRepository;
import lt.ca.javau11.repository.ReviewRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GameService {
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    
 // Find all games with sorting
    public List<Game> findAll(String sortBy) {
        List<Game> games = gameRepository.findAll();

        switch (sortBy) {
            case "newest":
                return games.stream().sorted(Comparator.comparing(Game::getReleaseDate).reversed()).collect(Collectors.toList());
            case "oldest":
                return games.stream().sorted(Comparator.comparing(Game::getReleaseDate)).collect(Collectors.toList());
            case "highestRating":
                return games.stream().sorted(Comparator.comparingDouble(this::calculateAverageRating).reversed()).collect(Collectors.toList());
            default:
                return games;
        }
    }
    
 // Find all games without sorting
    public List<Game> findAll() {
        return gameRepository.findAll();
    }

    // Find a game by ID
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
 // Calculate average rating for a game
    private double calculateAverageRating(Game game) {
        List<Review> reviews = reviewRepository.findByGameId(game.getId());
        if (reviews.isEmpty()) return 0.0;
        double totalRating = reviews.stream().mapToInt(Review::getRating).sum();
        return totalRating / reviews.size();
    }
    
}