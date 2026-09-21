package com.petproject.term_paper.util;

import com.petproject.term_paper.entity.*;
import com.petproject.term_paper.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EntityFinder {
    private final DealRepository dealRepository;
    private final PropertyRepository propertyRepository;
    private final OwnerRepository ownerRepository;
    private final UserRepository userRepository;

    public DealEntity findDeal(Long dealId) {
        return dealRepository.findById(dealId)
                .orElseThrow(() -> new EntityNotFoundException("Deal not found with id: " + dealId));
    }

    public PropertyEntity findProperty(Long propertyId) {
        return propertyRepository.findById(propertyId)
                .orElseThrow(() -> new EntityNotFoundException("Property not found with id: " + propertyId));
    }

    public OwnerEntity findOwner(Long ownerId) {
        return ownerRepository.findById(ownerId)
                .orElseThrow(() -> new EntityNotFoundException("Owner not found with id: " + ownerId));
    }

    public UserEntity findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
    }
}
