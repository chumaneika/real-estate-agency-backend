package com.petproject.term_paper.repository;

import com.petproject.term_paper.entity.UserEntity;
import com.petproject.term_paper.entity.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByEmail(String email);
    List<UserEntity> findAllByRoleOrderByUsername(UserRole role);
}
