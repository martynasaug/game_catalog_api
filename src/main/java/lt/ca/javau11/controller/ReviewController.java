// src/main/java/lt/ca/javau11/controller/ReviewController.java

package lt.ca.javau11.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lt.ca.javau11.dto.ReviewDTO;
import lt.ca.javau11.model.Review;
import lt.ca.javau11.service.ReviewService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private static final Logger logger = LoggerFactory.getLogger(ReviewController.class);

    @Autowired
    private ReviewService reviewService;

    // Fetch all reviews
    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews() {
        logger.info("Fetching all reviews...");
        List<Review> reviews = reviewService.findAll();
        return ResponseEntity.ok(reviews);
    }

    // Fetch reviews by gameId
    @GetMapping("/by-game/{gameId}")
    public ResponseEntity<List<Review>> getReviewsByGameId(@PathVariable Long gameId) {
        logger.info("Fetching reviews for game ID: {}", gameId);
        List<Review> reviews = reviewService.findReviewsByGameId(gameId);
        return ResponseEntity.ok(reviews);
    }

    // Fetch review by ID
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Review>> getReviewById(@PathVariable Long id) {
        logger.info("Fetching review with ID: {}", id);
        Optional<Review> review = reviewService.findById(id);
        return ResponseEntity.ok(review);
    }

    // Create a new review
    @PostMapping
    public ResponseEntity<Review> createReview(@Valid @RequestBody ReviewDTO reviewDTO) {
        logger.info("Incoming request to create review: {}", reviewDTO);
        Review savedReview = reviewService.save(reviewDTO);
        logger.info("Review created successfully: {}", savedReview);
        return ResponseEntity.ok(savedReview);
    }

    // Update an existing review
    @PutMapping("/{id}")
    public ResponseEntity<Optional<Review>> updateReview(@PathVariable Long id, @Valid @RequestBody ReviewDTO reviewDTO) {
        logger.info("Incoming request to update review with ID: {}", id);
        Optional<Review> updatedReview = reviewService.updateReview(id, reviewDTO);
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
        reviewService.delete(id);
        return ResponseEntity.noContent().build();
    }
}