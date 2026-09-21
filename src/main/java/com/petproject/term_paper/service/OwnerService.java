package com.petproject.term_paper.service;

import com.petproject.term_paper.dto.OwnerCreateRequest;
import com.petproject.term_paper.dto.OwnerResponse;
import com.petproject.term_paper.dto.OwnerUpdateRequest;
import com.petproject.term_paper.entity.OwnerEntity;
import com.petproject.term_paper.entity.enums.OwnerType;
import com.petproject.term_paper.repository.OwnerRepository;
import com.petproject.term_paper.repository.PropertyRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class OwnerService {
    private final OwnerRepository owners;
    private final PropertyRepository properties;

    @Transactional(readOnly = true)
    public List<OwnerResponse> getAllOwners() {
        return owners.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public OwnerResponse getOwnerById(Long id) {
        return toResponse(find(id));
    }

    public OwnerResponse createOwner(OwnerCreateRequest request) {
        if (request == null) throw new IllegalArgumentException("Owner data is required.");
        validate(request.getOwnerType(), request.getFirstName(), request.getLastName(),
                request.getCompanyName(), request.getEmail(), null);
        OwnerEntity owner = new OwnerEntity();
        apply(owner, request.getOwnerType(), request.getPhone(), request.getEmail(), request.getFirstName(),
                request.getLastName(), request.getMiddleName(), request.getCompanyName(), request.getTaxId(),
                request.getRegistrationNumber());
        return toResponse(owners.save(owner));
    }

    @Transactional
    public OwnerResponse updateOwner(Long id, OwnerUpdateRequest request) {
        if (request == null) throw new IllegalArgumentException("Owner data is required.");
        OwnerEntity owner = find(id);
        validate(request.getOwnerType(), request.getFirstName(), request.getLastName(),
                request.getCompanyName(), request.getEmail(), id);
        apply(owner, request.getOwnerType(), request.getPhone(), request.getEmail(), request.getFirstName(),
                request.getLastName(), request.getMiddleName(), request.getCompanyName(), request.getTaxId(),
                request.getRegistrationNumber());
        return toResponse(owner);
    }

    public void deleteOwner(Long id) {
        if (!owners.existsById(id)) throw new EntityNotFoundException("Owner not found with id: " + id);
        if (properties.existsByOwnerId(id)) {
            throw new OwnerInUseException("Owner cannot be deleted while properties are assigned to it.");
        }
        owners.deleteById(id);
    }

    private OwnerEntity find(Long id) {
        return owners.findById(id).orElseThrow(() -> new EntityNotFoundException("Owner not found with id: " + id));
    }

    private void validate(OwnerType type, String firstName, String lastName, String companyName, String email, Long id) {
        if (type == null) throw new IllegalArgumentException("Owner type is required.");
        if (type == OwnerType.INDIVIDUAL && (blank(firstName) || blank(lastName))) {
            throw new IllegalArgumentException("First name and last name are required for an individual owner.");
        }
        if (type == OwnerType.COMPANY && blank(companyName)) {
            throw new IllegalArgumentException("Company name is required for a company owner.");
        }
        String normalizedEmail = normalize(email);
        if (normalizedEmail != null && !normalizedEmail.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new IllegalArgumentException("Enter a valid owner email address.");
        }
        if (normalizedEmail != null && (id == null
                ? owners.existsByEmailIgnoreCase(normalizedEmail)
                : owners.existsByEmailIgnoreCaseAndIdNot(normalizedEmail, id))) {
            throw new IllegalArgumentException("Owner with this email already exists.");
        }
    }

    private void apply(OwnerEntity owner, OwnerType type, String phone, String email, String firstName,
                       String lastName, String middleName, String companyName, String taxId, String registrationNumber) {
        owner.setOwnerType(type);
        owner.setPhone(normalize(phone));
        owner.setEmail(normalizeLower(email));
        owner.setFirstName(type == OwnerType.INDIVIDUAL ? normalize(firstName) : null);
        owner.setLastName(type == OwnerType.INDIVIDUAL ? normalize(lastName) : null);
        owner.setMiddleName(type == OwnerType.INDIVIDUAL ? normalize(middleName) : null);
        owner.setCompanyName(type == OwnerType.COMPANY ? normalize(companyName) : null);
        owner.setTaxId(type == OwnerType.COMPANY ? normalize(taxId) : null);
        owner.setRegistrationNumber(type == OwnerType.COMPANY ? normalize(registrationNumber) : null);
    }

    private OwnerResponse toResponse(OwnerEntity owner) {
        return new OwnerResponse(owner.getId(), owner.getOwnerType(), owner.getPhone(), owner.getEmail(),
                owner.getFirstName(), owner.getLastName(), owner.getMiddleName(), owner.getCompanyName(),
                owner.getTaxId(), owner.getRegistrationNumber(), owner.getProperties() == null ? 0 : owner.getProperties().size());
    }

    private boolean blank(String value) { return value == null || value.isBlank(); }
    private String normalize(String value) { return blank(value) ? null : value.trim(); }
    private String normalizeLower(String value) {
        String normalized = normalize(value);
        return normalized == null ? null : normalized.toLowerCase();
    }

    public static class OwnerInUseException extends RuntimeException {
        public OwnerInUseException(String message) { super(message); }
    }
}
