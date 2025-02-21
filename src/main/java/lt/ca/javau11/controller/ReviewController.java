package lt.ca.javau11.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import lt.ca.javau11.dto.ReviewDTO;
import lt.ca.javau11.service.ReviewService;
import lt.ca.javau11.service.UserService;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    private static final Logger logger = LoggerFactory.getLogger(ReviewController.class);

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    // Fetch all reviews. Not used, maybe will use later
    @GetMapping
    public ResponseEntity<List<ReviewDTO>> getAllReviews() { 
        logger.info("Fetching all reviews...");
        List<ReviewDTO> reviews = reviewService.findAll();
        return ResponseEntity.ok(reviews);
    }

    // Fetch reviews by gameId
    @GetMapping("/by-game/{gameId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByGameId(@PathVariable Long gameId) { 
        logger.info("Fetching reviews for game ID: {}", gameId);
        List<ReviewDTO> reviews = reviewService.findReviewsByGameId(gameId);
        return ResponseEntity.ok(reviews);
    }

    // Fetch review by ID. Not used, maybe will use later
    @GetMapping("/{id}")
    public ResponseEntity<Optional<ReviewDTO>> getReviewById(@PathVariable Long id) {
        logger.info("Fetching review with ID: {}", id);
        Optional<ReviewDTO> review = reviewService.findById(id);
        return ResponseEntity.ok(review);
    }

    // Create a new review
    @PostMapping
    public ResponseEntity<ReviewDTO> createReview(@Valid @RequestBody ReviewDTO reviewDTO) {
        logger.info("Incoming request to create review: {}", reviewDTO);
        ReviewDTO savedReview = reviewService.save(reviewDTO);
        logger.info("Review created successfully: {}", savedReview);
        return ResponseEntity.ok(savedReview);
    }

    // Update an existing review
    @PutMapping("/{id}")
    public ResponseEntity<Optional<ReviewDTO>> updateReview(@PathVariable Long id, @Valid @RequestBody ReviewDTO reviewDTO) {
        logger.info("Incoming request to update review with ID: {}", id);
        Optional<ReviewDTO> updatedReview = reviewService.updateReview(id, reviewDTO);
        if (updatedReview.isPresent()) {
            logger.info("Review updated successfully: {}", updatedReview.get());
        } else {
            logger.error("Failed to update review with ID: {}", id);
        }
        return ResponseEntity.ok(updatedReview);
    }

    // Delete a review
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        logger.info("Deleting review with ID: {}", id);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean isAdmin = userService.isCurrentUserAdmin(username);

        try {
            // Check if the user is authorized to delete the review
            if (!isAdmin && !reviewService.isReviewOwner(id, username)) {
                logger.warn("User {} attempted to delete review {} without permission", username, id);
                return ResponseEntity.status(403).build(); // Forbidden
            }

            // Perform the deletion
            reviewService.delete(id);
            logger.info("Review with ID {} deleted successfully by user {}", id, username);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error while deleting review with ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(500).build(); // Internal Server Error
        }
    }
    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByUserId(@PathVariable Long userId) {
        List<ReviewDTO> reviews = reviewService.findReviewsByUserId(userId);
        return ResponseEntity.ok(reviews);
    }
}