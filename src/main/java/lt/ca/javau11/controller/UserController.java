package lt.ca.javau11.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import lt.ca.javau11.dto.ProfileDTO;
import lt.ca.javau11.dto.ReviewDTO;
import lt.ca.javau11.model.User;
import lt.ca.javau11.service.ReviewService;
import lt.ca.javau11.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

 @Autowired
 private UserService userService;
 
 @Autowired
 private ReviewService reviewService;

 @GetMapping("/{userId}")
 public ResponseEntity<User> getUserProfile(@PathVariable Long userId) {
     return userService.findById(userId)
             .map(ResponseEntity::ok)
             .orElseGet(() -> ResponseEntity.notFound().build());
 }

 @PutMapping("/{userId}")
 public ResponseEntity<?> updateProfile(
         @PathVariable Long userId,
         @RequestParam(required = false) MultipartFile file,
         @Valid @ModelAttribute ProfileDTO profileDTO,
         BindingResult bindingResult) throws IOException {

     if (bindingResult.hasErrors()) {
         Map<String, String> errors = new HashMap<>();
         bindingResult.getFieldErrors().forEach(error -> 
             errors.put(error.getField(), error.getDefaultMessage()));
         return ResponseEntity.badRequest().body(errors);
     }

     try {
         User updatedUser = userService.updateUserProfile(userId, profileDTO, file);
         return ResponseEntity.ok(updatedUser);
     } catch (DataIntegrityViolationException e) {
         return ResponseEntity.badRequest().body(
             Map.of("error", "Username or email already exists"));
     }
 }
 @GetMapping("/{userId}/reviews")
 public ResponseEntity<List<ReviewDTO>> getUserReviews(@PathVariable Long userId) {
     List<ReviewDTO> reviews = reviewService.findReviewsByUserId(userId);
     return ResponseEntity.ok(reviews);
 }

 
}
