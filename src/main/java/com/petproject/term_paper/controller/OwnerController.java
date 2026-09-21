package com.petproject.term_paper.controller;

import com.petproject.term_paper.dto.OwnerCreateRequest;
import com.petproject.term_paper.dto.OwnerResponse;
import com.petproject.term_paper.dto.OwnerUpdateRequest;
import com.petproject.term_paper.service.OwnerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/owners")
@PreAuthorize("hasRole('ADMIN')")
public class OwnerController {
    private final OwnerService ownerService;

    @GetMapping({"", "/get-all"})
    public List<OwnerResponse> getAllOwners() { return ownerService.getAllOwners(); }

    @GetMapping("/{id}")
    public OwnerResponse getOwnerById(@PathVariable Long id) { return ownerService.getOwnerById(id); }

    @PostMapping({"", "/create"})
    public ResponseEntity<OwnerResponse> createOwner(@RequestBody OwnerCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ownerService.createOwner(request));
    }

    @PutMapping("/{id}")
    public OwnerResponse updateOwner(@PathVariable Long id, @RequestBody OwnerUpdateRequest request) {
        return ownerService.updateOwner(id, request);
    }

    @DeleteMapping({"/{id}", "/delete/{id}"})
    public ResponseEntity<Void> deleteOwner(@PathVariable Long id) {
        ownerService.deleteOwner(id);
        return ResponseEntity.noContent().build();
    }
}
