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
    public ResponseEntity<Review> getReviewById(@PathVariable Long id) {
        logger.info("Fetching review with ID: {}", id);
        Optional<Review> review = reviewService.findById(id);
        return review.map(ResponseEntity::ok)
                     .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Create a new review
    @PostMapping
    public ResponseEntity<Review> createReview(@Valid @RequestBody ReviewDTO reviewDTO) {
        logger.info("Creating new review for game ID: {} by user ID: {}", reviewDTO.getGameId(), reviewDTO.getUserId());
        Review savedReview = reviewService.save(reviewDTO);
        return ResponseEntity.ok(savedReview);
    }

    // Update an existing review
    @PutMapping("/{id}")
    public ResponseEntity<Review> updateReview(@PathVariable Long id, @Valid @RequestBody ReviewDTO reviewDTO) {
        logger.info("Updating review ID: {}", id);
        Optional<Review> updatedReview = reviewService.updateReview(id, reviewDTO);
        return updatedReview.map(ResponseEntity::ok)
                            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete a review
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        logger.info("Deleting review ID: {}", id);
        if (reviewService.findById(id).isPresent()) {
            reviewService.delete(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}