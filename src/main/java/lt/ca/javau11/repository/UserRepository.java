package lt.ca.javau11.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import lt.ca.javau11.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}