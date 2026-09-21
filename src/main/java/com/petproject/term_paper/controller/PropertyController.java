package com.petproject.term_paper.controller;

import com.petproject.term_paper.dto.PropertyCreateRequest;
import com.petproject.term_paper.dto.PropertyDTO;
import com.petproject.term_paper.dto.PropertyUpdateRequest;
import com.petproject.term_paper.dto.mapping.PropertyMapping;
import com.petproject.term_paper.service.PropertyService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/properties")
public class PropertyController {
    private final PropertyService properties;
    private final PropertyMapping mapping;

    @GetMapping("/get-all")
    public List<PropertyDTO> getAllProperties() {
        return properties.getAllProperties().stream().map(mapping::toDTO).toList();
    }

    @GetMapping("/{id}")
    public PropertyDTO getPropertyById(@PathVariable Long id) { return mapping.toDTO(properties.getPropertyById(id)); }

    @PostMapping({"", "/create"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PropertyDTO> createProperty(@RequestBody PropertyCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mapping.toDTO(properties.createProperty(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public PropertyDTO updateProperty(@PathVariable Long id, @RequestBody PropertyUpdateRequest request) {
        return mapping.toDTO(properties.updateProperty(id, request));
    }

    @DeleteMapping({"/{id}", "/delete/{id}"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProperty(@PathVariable Long id) {
        properties.deleteProperty(id);
        return ResponseEntity.noContent().build();
    }
}
