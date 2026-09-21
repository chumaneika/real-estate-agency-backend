package com.petproject.term_paper.repository;

import com.petproject.term_paper.entity.PropertyEntity;
import com.petproject.term_paper.entity.enums.PropertyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;
import java.util.Optional;

public interface PropertyRepository extends JpaRepository<PropertyEntity, Long> {
    @Override
    @EntityGraph(attributePaths = {"owner", "agent", "imageUrls"})
    List<PropertyEntity> findAll();

    @Override
    @EntityGraph(attributePaths = {"owner", "agent", "imageUrls"})
    Optional<PropertyEntity> findById(Long id);

    long countByType(PropertyType type);

    @Query("select coalesce(avg(p.price), 0) from PropertyEntity p where p.price is not null")
    Double averagePrice();

    boolean existsByOwnerId(Long ownerId);
    boolean existsByAgentId(Long agentId);

    @EntityGraph(attributePaths = {"owner", "agent", "imageUrls"})
    List<PropertyEntity> findAllByAgentUsernameOrderByIdDesc(String username);
}
