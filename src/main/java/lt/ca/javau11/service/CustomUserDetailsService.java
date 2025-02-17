// src/main/java/lt/ca/javau11/service/CustomUserDetailsService.java

package lt.ca.javau11.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lt.ca.javau11.model.User;
import lt.ca.javau11.repository.UserRepository;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return new CustomUserDetails(user);
    }

    public static class CustomUserDetails extends org.springframework.security.core.userdetails.User implements Serializable {
        private static final long serialVersionUID = 1L; // Add this line

        private final Long id;
        private final List<String> roles;

        public CustomUserDetails(User user) {
            super(user.getUsername(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
            this.id = user.getId();
            this.roles = user.getRoles().stream().toList();
        }

        public Long getId() {
            return id;
        }

        public List<String> getRoles() {
            return roles;
        }

        private static List<GrantedAuthority> mapRolesToAuthorities(Set<String> roles) {
            return roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }
    }
}