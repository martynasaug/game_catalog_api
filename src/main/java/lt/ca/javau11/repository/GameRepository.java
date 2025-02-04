package lt.ca.javau11.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import lt.ca.javau11.model.Game;

import java.util.List;
import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findAll();
    Optional<Game> findById(Long id);
}