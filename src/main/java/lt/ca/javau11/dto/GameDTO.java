package lt.ca.javau11.dto;

import java.time.LocalDate;
import java.util.Set;

public class GameDTO {

    private Long id;
    private String title;
    private String description;
    private String platform;
    private LocalDate releaseDate;
    private Set<ReviewDTO> reviews;

    public GameDTO() {}

    public GameDTO(Long id, String title, String description, String platform, LocalDate releaseDate, Set<ReviewDTO> reviews) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.platform = platform;
        this.releaseDate = releaseDate;
        this.reviews = reviews;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Set<ReviewDTO> getReviews() {
        return reviews;
    }

    public void setReviews(Set<ReviewDTO> reviews) {
        this.reviews = reviews;
    }
}