package com.petproject.term_paper.service;

import com.petproject.term_paper.entity.OwnerEntity;
import com.petproject.term_paper.entity.PropertyEntity;
import com.petproject.term_paper.repository.OwnerRepository;
import com.petproject.term_paper.repository.PropertyRepository;
import com.petproject.term_paper.util.EntityFinder;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class OwnerService {
    private final OwnerRepository ownerRepository;
    private final PropertyRepository propertyRepository;
    private final EntityFinder entityFinder;

    public List<OwnerEntity> getAllOwners() {
        List<OwnerEntity> ownerEntities = new ArrayList<>();
        ownerRepository.findAll().forEach(ownerEntities::add);
        return ownerEntities;
    }

    public OwnerEntity getOwnerById(Long id) {
        return entityFinder.findOwner(id);
    }

    public OwnerEntity createOwner(OwnerEntity ownerEntity) {
        if (ownerRepository.existsByEmail(ownerEntity.getEmail())) {
            throw new IllegalArgumentException("Owner with this email already exists");
        }

        return ownerRepository.save(ownerEntity);
    }

    public void deleteOwner(Long id) {
        OwnerEntity ownerEntity = ownerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Owner not found"));

        for (PropertyEntity propertyEntity : ownerEntity.getProperties()) {
            propertyEntity.setOwnerEntity(null);
            propertyRepository.save(propertyEntity);
        }

        ownerRepository.deleteById(id);
    }

    public PropertyEntity addPropertyToOwner(Long ownerId, Long propertyId) {
        OwnerEntity ownerEntity = entityFinder.findOwner(ownerId);
        PropertyEntity propertyEntity = entityFinder.findProperty(propertyId);

        List<PropertyEntity> ownerProperties = ownerEntity.getProperties();
        ownerProperties.add(propertyEntity);
        propertyEntity.setOwnerEntity(ownerEntity);
        ownerEntity.setProperties(ownerProperties);

        ownerRepository.save(ownerEntity);
        return propertyEntity;
    }

    public void deletePropertyFromOwner(Long ownerId, Long propertyId) {
        OwnerEntity ownerEntity = entityFinder.findOwner(ownerId);
        PropertyEntity propertyEntity = entityFinder.findProperty(propertyId);

        ownerEntity.getProperties().remove(propertyEntity);
        propertyEntity.setOwnerEntity(null); // Разрываем связь

        // Сохраняем изменения
        propertyRepository.save(propertyEntity);
        ownerRepository.save(ownerEntity);
    }

}
