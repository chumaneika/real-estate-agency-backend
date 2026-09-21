package com.petproject.term_paper.repository;

import com.petproject.term_paper.entity.PropertyEntity;
import com.petproject.term_paper.entity.enums.PropertyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PropertyRepository extends JpaRepository<PropertyEntity, Long> {
    long countByType(PropertyType type);

    @Query("select coalesce(avg(p.price), 0) from PropertyEntity p where p.price is not null")
    Double averagePrice();
}
