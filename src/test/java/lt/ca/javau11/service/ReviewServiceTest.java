package lt.ca.javau11.service;

import lt.ca.javau11.dto.ReviewDTO;
import lt.ca.javau11.model.Game;
import lt.ca.javau11.model.Review;
import lt.ca.javau11.model.User;
import lt.ca.javau11.repository.GameRepository;
import lt.ca.javau11.repository.ReviewRepository;
import lt.ca.javau11.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReviewServiceTest {

    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GameRepository gameRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll() {
        Review review = new Review();
        when(reviewRepository.findAll()).thenReturn(List.of(review));

        List<Review> reviews = reviewService.findAll();

        assertNotNull(reviews);
        assertEquals(1, reviews.size());
    }

    @Test
    void findById_Found() {
        Review review = new Review();
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        Optional<Review> foundReview = reviewService.findById(1L);

        assertTrue(foundReview.isPresent());
    }

    @Test
    void findById_NotFound() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Review> foundReview = reviewService.findById(1L);

        assertFalse(foundReview.isPresent());
    }

    @Test
    void save_Success() {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setComment("Great game!");
        reviewDTO.setRating(5);
        reviewDTO.setUserId(1L);
        reviewDTO.setGameId(1L);

        User user = new User();
        Game game = new Game();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));

        Review review = new Review();
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        Review savedReview = reviewService.save(reviewDTO);

        assertNotNull(savedReview);
    }
}