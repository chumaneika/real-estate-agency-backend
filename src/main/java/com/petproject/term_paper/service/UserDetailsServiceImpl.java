package com.petproject.term_paper.service;

import com.petproject.term_paper.config.UserDetailsImpl;
import com.petproject.term_paper.dto.UserDTO;
import com.petproject.term_paper.dto.mapping.UserMapping;
import com.petproject.term_paper.models.User;
import com.petproject.term_paper.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserMapping userMapping;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);

        return user.map(UserDetailsImpl::new)
                .orElseThrow(() -> new UsernameNotFoundException(username + " not found"));
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public UserDTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapping::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }
}
