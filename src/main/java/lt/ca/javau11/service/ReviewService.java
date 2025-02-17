// src/main/java/lt/ca/javau11/service/ReviewService.java

package lt.ca.javau11.service;

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

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameRepository gameRepository;

    // Find all reviews
    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    // Find review by ID
    public Optional<Review> findById(Long id) {
        return reviewRepository.findById(id);
    }

    // Find reviews by game ID
    public List<Review> findReviewsByGameId(Long gameId) {
        return reviewRepository.findByGameId(gameId);
    }

    // Create and save a new review
    public Review save(ReviewDTO reviewDTO) {
        Optional<User> optionalUser = userRepository.findById(reviewDTO.getUserId());
        Optional<Game> optionalGame = gameRepository.findById(reviewDTO.getGameId());

        if (!optionalUser.isPresent()) {
            throw new RuntimeException("User not found");
        }
        if (!optionalGame.isPresent()) {
            throw new RuntimeException("Game not found");
        }

        User user = optionalUser.get(); // Ensure the user is fetched correctly
        Game game = optionalGame.get();

        Review review = new Review();
        review.setComment(reviewDTO.getComment());
        review.setRating(reviewDTO.getRating());
        review.setUser(user); // Associate the review with the user
        review.setGame(game);

        return reviewRepository.save(review);
    }

    // Update an existing review
    public Optional<Review> updateReview(Long id, ReviewDTO reviewDTO) {
        return reviewRepository.findById(id).map(existingReview -> {
            existingReview.setComment(reviewDTO.getComment());
            existingReview.setRating(reviewDTO.getRating());
            return reviewRepository.save(existingReview);
        });
    }

    // Delete a review
    public void delete(Long id) {
        reviewRepository.deleteById(id);
    }
}