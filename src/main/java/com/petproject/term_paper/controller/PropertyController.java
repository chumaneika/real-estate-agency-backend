package com.petproject.term_paper.controller;

import com.petproject.term_paper.dto.PropertyDTO;
import com.petproject.term_paper.dto.mapping.PropertyMapping;
import com.petproject.term_paper.entity.PropertyEntity;
import com.petproject.term_paper.service.PropertyService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import jakarta.persistence.EntityNotFoundException;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/properties")
public class PropertyController {
    private final PropertyService propertyService;
    private final PropertyMapping propertyMapping;

    @GetMapping("/get-all")
    public ResponseEntity<List<PropertyDTO>> getAllProperties() {
        return ResponseEntity.ok(propertyService.getAllProperties().stream().map(propertyMapping::toDTO).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPropertyById(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(propertyMapping.toDTO(propertyService.getPropertyById(id)));
        } catch (EntityNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Property not found."));
        }
    }

    @PostMapping("/create")
    public ResponseEntity<PropertyDTO> createProperty(@RequestBody PropertyEntity propertyEntity) {
        PropertyEntity createdPropertyEntity = propertyService.createProperty(propertyEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(propertyMapping.toDTO(createdPropertyEntity));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleInvalidProperty(IllegalArgumentException exception) {
        return ResponseEntity.badRequest().body(Map.of("message", exception.getMessage()));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProperty(@PathVariable("id") Long id) {
        propertyService.deleteProperty(id);
        return ResponseEntity.noContent().build();
    }
}
