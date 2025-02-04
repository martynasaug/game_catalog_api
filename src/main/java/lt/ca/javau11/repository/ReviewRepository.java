package lt.ca.javau11.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import lt.ca.javau11.model.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAll();
    Optional<Review> findById(Long id);
}