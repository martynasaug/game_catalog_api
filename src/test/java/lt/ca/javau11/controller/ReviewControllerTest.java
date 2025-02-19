package lt.ca.javau11.controller;

import lt.ca.javau11.dto.ReviewDTO;
import lt.ca.javau11.model.Review;
import lt.ca.javau11.service.ReviewService;
import lt.ca.javau11.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReviewControllerTest {

    @InjectMocks
    private ReviewController reviewController;

    @Mock
    private ReviewService reviewService;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllReviews() {
        Review review = new Review();
        when(reviewService.findAll()).thenReturn(List.of(review));
        ResponseEntity<List<Review>> response = reviewController.getAllReviews();
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getReviewById_Found() {
        Review review = new Review();
        when(reviewService.findById(1L)).thenReturn(Optional.of(review));
        ResponseEntity<Optional<Review>> response = reviewController.getReviewById(1L);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isPresent());
    }

    @Test
    void getReviewById_NotFound() {
        when(reviewService.findById(1L)).thenReturn(Optional.empty());
        ResponseEntity<Optional<Review>> response = reviewController.getReviewById(1L);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isPresent());
    }

    @Test
    void createReview_Success() {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setComment("Great game!");
        reviewDTO.setRating(5);
        reviewDTO.setUserId(1L);
        reviewDTO.setGameId(1L);
        Review review = new Review();
        when(reviewService.save(reviewDTO)).thenReturn(review);
        ResponseEntity<Review> response = reviewController.createReview(reviewDTO);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }
}