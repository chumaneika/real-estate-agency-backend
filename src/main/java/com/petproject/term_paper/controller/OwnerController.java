package com.petproject.term_paper.controller;

import com.petproject.term_paper.entity.OwnerEntity;
import com.petproject.term_paper.entity.PropertyEntity;
import com.petproject.term_paper.service.OwnerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/owners")
public class OwnerController {
    private final OwnerService ownerService;

    @GetMapping("/get-all")
    public ResponseEntity<List<OwnerEntity>> getAllOwners() {
        return ResponseEntity.ok(ownerService.getAllOwners());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OwnerEntity> getOwnerById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(ownerService.getOwnerById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<OwnerEntity> createOwner(@RequestBody OwnerEntity ownerEntity) {
        OwnerEntity createdOwnerEntity = ownerService.createOwner(ownerEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOwnerEntity);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteOwner(@PathVariable("id") Long id) {
        ownerService.deleteOwner(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/assign-property/{ownerId}/{propertyId}")
    public ResponseEntity<PropertyEntity> addPropertyToOwner(@PathVariable("ownerId") Long ownerId, @PathVariable("propertyId") Long propertyId) {
        PropertyEntity addedPropertyEntity = ownerService.addPropertyToOwner(ownerId, propertyId);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedPropertyEntity);
    }

    @DeleteMapping("/delete-property/{ownerId}/{propertyId}")
    public ResponseEntity<Void> deletePropertyFromOwner(@PathVariable("ownerId") Long ownerId, @PathVariable("propertyId") Long propertyId) {
        ownerService.deletePropertyFromOwner(ownerId, propertyId);
        return ResponseEntity.noContent().build();
    }
}
