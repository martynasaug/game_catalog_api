// src/main/java/lt/ca/javau11/service/CustomUserDetailsService.java

package lt.ca.javau11.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lt.ca.javau11.model.User;
import lt.ca.javau11.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Loading user by username: {}", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        // Map roles to GrantedAuthorities
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // Use the authorities explicitly here (optional, for debugging purposes)
        logger.debug("User authorities: {}", authorities);

        return new CustomUserDetails(user);
    }

    public static class CustomUserDetails extends org.springframework.security.core.userdetails.User implements Serializable {

        private static final long serialVersionUID = 1L; // Resolve serialization warnings

        private final Long id;

        public CustomUserDetails(User user) {
            super(user.getUsername(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
            this.id = user.getId();
        }

        public Long getId() {
            return id;
        }

        private static List<SimpleGrantedAuthority> mapRolesToAuthorities(Set<String> roles) {
            return roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }
    }
}