package lt.ca.javau11.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lt.ca.javau11.dto.ReviewDTO;
import lt.ca.javau11.model.Game;
import lt.ca.javau11.model.Review;
import lt.ca.javau11.model.User;
import lt.ca.javau11.service.GameService;
import lt.ca.javau11.service.ReviewService;
import lt.ca.javau11.service.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    @Autowired
    private GameService gameService;

    @GetMapping
    public List<Review> getAllReviews() {
        return reviewService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable Long id) {
        return reviewService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Review> createReview(@RequestBody ReviewDTO reviewDTO) {
        Optional<User> user = userService.findById(reviewDTO.getUserId());
        Optional<Game> game = gameService.findById(reviewDTO.getGameId());

        if (!user.isPresent() || !game.isPresent()) {
            return ResponseEntity.badRequest().body(null);
        }

        Review review = new Review(
            reviewDTO.getComment(),
            reviewDTO.getRating(),
            user.get(),
            game.get()
        );

        Review savedReview = reviewService.save(review);
        return ResponseEntity.ok(savedReview);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Review> updateReview(@PathVariable Long id, @RequestBody ReviewDTO reviewDTO) {
        return reviewService.findById(id)
                .map(existingReview -> {
                    existingReview.setComment(reviewDTO.getComment());
                    existingReview.setRating(reviewDTO.getRating());
                    return ResponseEntity.ok(reviewService.save(existingReview));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.delete(id);
        return ResponseEntity.noContent().build();
    }
}