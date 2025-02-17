package lt.ca.javau11.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import lt.ca.javau11.model.Review;
import lt.ca.javau11.model.User;
import lt.ca.javau11.model.Game;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByGameId(Long gameId);

    @Query("SELECT u FROM User u WHERE u.id = :id")
    Optional<User> findUserById(@Param("id") Long id);

    @Query("SELECT g FROM Game g WHERE g.id = :id")
    Optional<Game> findGameById(@Param("id") Long id);
}
