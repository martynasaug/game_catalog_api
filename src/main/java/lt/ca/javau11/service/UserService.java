package lt.ca.javau11.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lt.ca.javau11.dto.ProfileDTO;
import lt.ca.javau11.model.User;
import lt.ca.javau11.repository.UserRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public boolean isCurrentUserAdmin(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.map(u -> u.getRoles().contains("ROLE_ADMIN")).orElse(false);
    }
    public User updateUserProfile(Long userId, ProfileDTO profileDTO, MultipartFile file) throws IOException {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            
            user.setUsername(profileDTO.getUsername());
            user.setEmail(profileDTO.getEmail());
            user.setBio(profileDTO.getBio());

            if (file != null && !file.isEmpty()) {
                byte[] bytes = file.getBytes();
                Path path = Paths.get("src/main/resources/static/images/" + file.getOriginalFilename());
                Files.write(path, bytes);
                user.setProfileImageUrl("/images/" + file.getOriginalFilename());
            }

            return userRepository.save(user);
        } else {
            throw new RuntimeException("User not found with ID: " + userId);
        }
    }
    
}