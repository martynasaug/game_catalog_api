package lt.ca.javau11.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lt.ca.javau11.dto.ReviewDTO;
import lt.ca.javau11.model.Game;
import lt.ca.javau11.model.Review;
import lt.ca.javau11.model.User;
import lt.ca.javau11.repository.GameRepository;
import lt.ca.javau11.repository.ReviewRepository;
import lt.ca.javau11.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewService {
    private static final Logger logger = LoggerFactory.getLogger(ReviewService.class);

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameRepository gameRepository;

    // Find all reviews. Not used, maybe will use later
    public List<ReviewDTO> findAll() {
        logger.info("Fetching all reviews...");
        return reviewRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Find review by ID. Not used, maybe will use later
    public Optional<ReviewDTO> findById(Long id) {
        logger.info("Fetching review with ID: {}", id);
        return reviewRepository.findById(id)
                .map(this::convertToDTO);
    }

    // Find reviews by game ID
    public List<ReviewDTO> findReviewsByGameId(Long gameId) {
        logger.info("Fetching reviews for game ID: {}", gameId);
        return reviewRepository.findByGameId(gameId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Create and save a new review
    public ReviewDTO save(ReviewDTO reviewDTO) {
        logger.info("Creating new review for game ID: {} by user ID: {}", reviewDTO.getGameId(), reviewDTO.getUserId());

        Optional<User> optionalUser = userRepository.findById(reviewDTO.getUserId());
        Optional<Game> optionalGame = gameRepository.findById(reviewDTO.getGameId());

        if (!optionalUser.isPresent()) {
            logger.error("User not found with ID: {}", reviewDTO.getUserId());
            throw new RuntimeException("User not found");
        }

        if (!optionalGame.isPresent()) {
            logger.error("Game not found with ID: {}", reviewDTO.getGameId());
            throw new RuntimeException("Game not found");
        }

        User user = optionalUser.get();
        Game game = optionalGame.get();

        Review review = new Review();
        review.setComment(reviewDTO.getComment());
        review.setRating(reviewDTO.getRating());
        review.setUser(user); // Associate the review with the user
        review.setGame(game); // Associate the review with the game

        Review savedReview = reviewRepository.save(review);
        logger.info("Review created successfully for user: {} and game: {}", user.getUsername(), game.getTitle());

        return convertToDTO(savedReview);
    }

    // Update an existing review
    public Optional<ReviewDTO> updateReview(Long id, ReviewDTO reviewDTO) {
        logger.info("Updating review with ID: {}", id);
        return reviewRepository.findById(id).map(existingReview -> {
            existingReview.setComment(reviewDTO.getComment());
            existingReview.setRating(reviewDTO.getRating());
            Review updatedReview = reviewRepository.save(existingReview);
            logger.info("Review updated successfully with ID: {}", id);
            return convertToDTO(updatedReview);
        });
    }

    // Delete a review
    public void delete(Long id) {
        logger.info("Deleting review with ID: {}", id);
        reviewRepository.deleteById(id);
    }

    // Check if the user is the owner of the review
    public boolean isReviewOwner(Long reviewId, String username) {
        Optional<Review> review = reviewRepository.findById(reviewId);
        if (review.isEmpty()) {
            logger.warn("Review with ID {} does not exist", reviewId);
            return false;
        }
        return review.get().getUser().getUsername().equals(username);
    }

    // Find reviews by user ID
    public List<ReviewDTO> findReviewsByUserId(Long userId) {
        logger.info("Fetching reviews for user ID: {}", userId);
        return reviewRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private ReviewDTO convertToDTO(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setComment(review.getComment());
        dto.setRating(review.getRating());
        dto.setUserId(review.getUser().getId());
        dto.setUsername(review.getUser().getUsername());
        dto.setGameId(review.getGame().getId());
        dto.setGameTitle(review.getGame().getTitle());
        return dto;
    }
}