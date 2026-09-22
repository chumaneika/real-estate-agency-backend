package com.petproject.term_paper.service;

import com.petproject.term_paper.config.UserDetailsImpl;
import com.petproject.term_paper.dto.UserDTO;
import com.petproject.term_paper.dto.mapping.UserMapping;
import com.petproject.term_paper.entity.UserEntity;
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
        Optional<UserEntity> user = userRepository.findByUsername(username);

        return user.map(UserDetailsImpl::new)
                .orElseThrow(() -> new UsernameNotFoundException(username + " not found"));
    }

    public UserEntity createUser(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    public Optional<UserEntity> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<UserEntity> findByEmail(String email) {
        return userRepository.findByEmail(email.toLowerCase());
    }

    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public UserDTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapping::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }

}
