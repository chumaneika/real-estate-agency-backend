package com.petproject.term_paper.controller;

import com.petproject.term_paper.dto.DealDTO;
import com.petproject.term_paper.dto.DealRequest;
import com.petproject.term_paper.service.DealService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/deals")
@AllArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class DealController {
    private final DealService deals;

    @GetMapping("/{id}") public DealDTO getDealById(@PathVariable Long id) { return deals.getDealById(id); }
    @GetMapping("/get-all") public List<DealDTO> getAllDeals() { return deals.getAllDeals(); }
    @PostMapping("/create") public ResponseEntity<DealDTO> createDeal(@RequestBody DealRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(deals.createDeal(request));
    }
    @PutMapping("/{id}") public DealDTO updateDeal(@PathVariable Long id, @RequestBody DealRequest request) {
        return deals.updateDeal(id, request);
    }
    @DeleteMapping("/delete/{id}") public ResponseEntity<Void> deleteDeal(@PathVariable Long id) {
        deals.deleteDeal(id);
        return ResponseEntity.noContent().build();
    }
}
