package com.petproject.term_paper.controller;

import com.petproject.term_paper.entity.DealEntity;
import com.petproject.term_paper.service.DealService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/deals")
@AllArgsConstructor
public class DealController {
    private final DealService dealService;

    @GetMapping("/{id}")
    public ResponseEntity<DealEntity> getDealById(@PathVariable("id") Long id) {
        DealEntity foundDealEntity = dealService.getDealById(id);
        return ResponseEntity.ok(foundDealEntity);
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<DealEntity>> getAllDeals() {
        return ResponseEntity.ok(dealService.getAllDeals());
    }

    @PostMapping("/create")
    public ResponseEntity<DealEntity> createDeal(@RequestBody DealEntity dealEntity) {
        DealEntity createdDealEntity = dealService.createDeal(dealEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDealEntity);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteDeal(@PathVariable("id") Long id) {
        dealService.deleteDeal(id);
        return ResponseEntity.noContent().build();
    }
}
