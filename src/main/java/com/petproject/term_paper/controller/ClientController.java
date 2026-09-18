package com.petproject.term_paper.controller;

import com.petproject.term_paper.entity.ClientEntity;
import com.petproject.term_paper.entity.DealEntity;
import com.petproject.term_paper.service.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clients")
@AllArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @GetMapping("/{id}")
    public ResponseEntity<ClientEntity> getClientById(@PathVariable("id") Long id) {
        ClientEntity foundClientEntity = clientService.getClientById(id);
        return ResponseEntity.ok(foundClientEntity);
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<ClientEntity>> getAllClients() {
        return ResponseEntity.ok(clientService.getAllClients());
    }

    @PostMapping("/create")
    public ResponseEntity<ClientEntity> createClient(@RequestBody ClientEntity clientEntity) {
        ClientEntity createdClientEntity = clientService.createClient(clientEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdClientEntity);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable("id") Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/assign-deal/{client-id}/{deal-id}")
    public ResponseEntity<DealEntity> assignDealToClient(@PathVariable("client-id") Long clientId, @PathVariable("deal-id") Long dealId) {
        DealEntity addedDealEntity = clientService.assignDealToClient(clientId, dealId);
        return ResponseEntity.ok(addedDealEntity);
    }

    @DeleteMapping("/delete-deal/{client-id}/{deal-id}")
    public ResponseEntity<Void> removeDealFromClient(@PathVariable("client-id") Long clientId, @PathVariable("deal-id") Long dealId) {
        clientService.removeDealFromClient(clientId, dealId);
        return ResponseEntity.noContent().build();
    }

}
