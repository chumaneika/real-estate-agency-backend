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
public class PropertyService {
    private final PropertyRepository propertyRepository;
    private final OwnerRepository ownerRepository;
    private final EntityFinder entityFinder;

    public List<PropertyEntity> getAllProperties() {
        List<PropertyEntity> properties = new ArrayList<>();
        propertyRepository.findAll().forEach(properties::add);
        return properties;
    }

    public PropertyEntity getPropertyById(Long id) {
        return entityFinder.findProperty(id);
    }

    public PropertyEntity createProperty(PropertyEntity propertyEntity) {
        propertyEntity.setImageUrls(normalizeImageUrls(propertyEntity.getImageUrls()));
        return propertyRepository.save(propertyEntity);
    }

    /**
     * Cloudinary uploads are performed by the client.  The API receives only
     * the resulting delivery URLs and persists them in their display order.
     */
    private List<String> normalizeImageUrls(List<String> imageUrls) {
        if (imageUrls == null) {
            return new ArrayList<>();
        }

        List<String> normalizedUrls = new ArrayList<>(imageUrls.size());
        for (String imageUrl : imageUrls) {
            if (imageUrl == null || imageUrl.isBlank()) {
                throw new IllegalArgumentException("Image URLs must not be blank");
            }
            normalizedUrls.add(imageUrl.trim());
        }
        return normalizedUrls;
    }

    public void deleteProperty(Long propertyId) {
        PropertyEntity propertyEntity = entityFinder.findProperty(propertyId);

        if (propertyEntity.getOwnerEntity() == null) {
            propertyRepository.deleteById(propertyId);
        } else {
            OwnerEntity ownerEntity = ownerRepository.findById(propertyEntity.getOwnerEntity().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Owner not found with id: " + propertyEntity.getOwnerEntity().getId()));

            List<PropertyEntity> propertiesOwner = ownerEntity.getProperties();
            propertiesOwner.remove(propertyEntity);
            ownerEntity.setProperties(propertiesOwner);

            ownerRepository.save(ownerEntity);

            propertyRepository.deleteById(propertyId);

        }
    }

    public void assignForOwner(Long propertyId, Long ownerId) {
        PropertyEntity propertyEntity = entityFinder.findProperty(propertyId);
        OwnerEntity ownerEntity = entityFinder.findOwner(ownerId);

        List<PropertyEntity> ownerProperties = ownerEntity.getProperties();
        ownerProperties.add(propertyEntity);
        ownerEntity.setProperties(ownerProperties);
        propertyEntity.setOwnerEntity(ownerEntity);

        ownerRepository.save(ownerEntity);
    }

    public void removeForOwner(Long propertyId, Long ownerId) {
        PropertyEntity propertyEntity = entityFinder.findProperty(propertyId);
        OwnerEntity ownerEntity = entityFinder.findOwner(ownerId);

        List<PropertyEntity> propertiesOwner = ownerEntity.getProperties();
        propertiesOwner.remove(propertyEntity);
        ownerEntity.setProperties(propertiesOwner);

        propertyRepository.save(propertyEntity);
        ownerRepository.save(ownerEntity);
    }
}
