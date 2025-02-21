package lt.ca.javau11.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ReviewDTO {
    @NotNull(message = "Comment is required")
    private String comment;

    @Min(value = 1, message = "Rating must be at least 1")
    @NotNull(message = "Rating is required")
    private int rating;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Game ID is required")
    private Long gameId;

    private String username;
    private String gameTitle;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGameTitle() {
        return gameTitle;
    }

    public void setGameTitle(String gameTitle) {
        this.gameTitle = gameTitle;
    }
}