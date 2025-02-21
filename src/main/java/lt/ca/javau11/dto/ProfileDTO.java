package lt.ca.javau11.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ProfileDTO {

	@NotNull
    @Size(min = 3, max = 50)
	private String username;
	
    @Email(message = "Invalid email format")
    @Column(unique = true)
    private String email;
    
	@Size(max = 500, message = "Bio cannot exceed 500 characters")
    private String bio;
    
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getBio() {
		return bio;
	}
	public void setBio(String bio) {
		this.bio = bio;
	}
    
    
}
