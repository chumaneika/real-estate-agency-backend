package com.petproject.term_paper.service;

import com.petproject.term_paper.dto.PropertyCreateRequest;
import com.petproject.term_paper.dto.PropertyUpdateRequest;
import com.petproject.term_paper.entity.PropertyEntity;
import com.petproject.term_paper.entity.UserEntity;
import com.petproject.term_paper.entity.enums.UserRole;
import com.petproject.term_paper.repository.OwnerRepository;
import com.petproject.term_paper.repository.PropertyRepository;
import com.petproject.term_paper.repository.UserRepository;
import com.petproject.term_paper.repository.ViewingRequestRepository;
import com.petproject.term_paper.repository.DealRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class PropertyService {
    private final PropertyRepository properties;
    private final OwnerRepository owners;
    private final UserRepository users;
    private final ViewingRequestRepository viewingRequests;
    private final DealRepository deals;

    @Transactional(readOnly = true)
    public List<PropertyEntity> getAllProperties() { return properties.findAll(); }

    @Transactional(readOnly = true)
    public PropertyEntity getPropertyById(Long id) {
        return properties.findById(id).orElseThrow(() -> new EntityNotFoundException("Property not found with id: " + id));
    }

    public PropertyEntity createProperty(PropertyCreateRequest request) {
        if (request == null) throw new IllegalArgumentException("Property data is required.");
        PropertyEntity property = new PropertyEntity();
        apply(property, request.getTitle(), request.getDescription(), request.getAddress(), request.getArea(),
                request.getRooms(), request.getPrice(), request.getType(), request.getImageUrls(),
                request.getOwnerId(), request.getAgentId());
        return properties.save(property);
    }

    @Transactional
    public PropertyEntity updateProperty(Long id, PropertyUpdateRequest request) {
        if (request == null) throw new IllegalArgumentException("Property data is required.");
        PropertyEntity property = getPropertyById(id);
        apply(property, request.getTitle(), request.getDescription(), request.getAddress(), request.getArea(),
                request.getRooms(), request.getPrice(), request.getType(), request.getImageUrls(),
                request.getOwnerId(), request.getAgentId());
        return property;
    }

    public void deleteProperty(Long id) {
        if (!properties.existsById(id)) throw new EntityNotFoundException("Property not found with id: " + id);
        if (viewingRequests.existsByPropertyId(id) || deals.existsByPropertyId(id)) {
            throw new PropertyInUseException("Property cannot be deleted while viewing requests or deals are assigned to it.");
        }
        properties.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<PropertyEntity> getAssignedProperties(String username) {
        return properties.findAllByAgentUsernameOrderByIdDesc(username);
    }

    private void apply(PropertyEntity property, String title, String description, String address, Double area,
                       Integer rooms, Double price, com.petproject.term_paper.entity.enums.PropertyType type,
                       List<String> imageUrls, Long ownerId, Long agentId) {
        if (ownerId == null) throw new IllegalArgumentException("Owner is required.");
        if (address == null || address.isBlank()) throw new IllegalArgumentException("Address is required.");
        if (area != null && area <= 0) throw new IllegalArgumentException("Area must be greater than zero.");
        if (rooms != null && rooms < 0) throw new IllegalArgumentException("Rooms cannot be negative.");
        if (price != null && price < 0) throw new IllegalArgumentException("Price cannot be negative.");
        property.setTitle(normalize(title));
        property.setDescription(normalize(description));
        property.setAddress(address.trim());
        property.setArea(area);
        property.setRooms(rooms);
        property.setPrice(price);
        property.setType(type);
        property.setImageUrls(normalizeImageUrls(imageUrls));
        property.setOwner(owners.findById(ownerId)
                .orElseThrow(() -> new EntityNotFoundException("Owner not found with id: " + ownerId)));
        property.setAgent(resolveAgent(agentId));
    }

    private UserEntity resolveAgent(Long agentId) {
        if (agentId == null) return null;
        UserEntity agent = users.findById(agentId)
                .orElseThrow(() -> new EntityNotFoundException("Agent not found with id: " + agentId));
        if (agent.getRole() != UserRole.AGENT) {
            throw new IllegalArgumentException("Only a user with the AGENT role can be assigned to a property.");
        }
        if (!agent.isEnabled()) throw new IllegalArgumentException("The selected agent account is disabled.");
        return agent;
    }

    private List<String> normalizeImageUrls(List<String> imageUrls) {
        if (imageUrls == null) return new ArrayList<>();
        List<String> normalized = new ArrayList<>();
        for (String url : imageUrls) {
            if (url == null || url.isBlank()) throw new IllegalArgumentException("Image URLs must not be blank.");
            normalized.add(url.trim());
        }
        return normalized;
    }

    private String normalize(String value) { return value == null || value.isBlank() ? null : value.trim(); }

    public static class PropertyInUseException extends RuntimeException {
        public PropertyInUseException(String message) { super(message); }
    }
}
