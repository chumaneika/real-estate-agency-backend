package com.petproject.term_paper.service;

import com.petproject.term_paper.entity.ClientEntity;
import com.petproject.term_paper.entity.DealEntity;
import com.petproject.term_paper.repository.ClientRepository;
import com.petproject.term_paper.repository.DealRepository;
import com.petproject.term_paper.util.EntityFinder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;
    private final DealRepository dealRepository;
    private final EntityFinder entityFinder;

    public ClientEntity getClientById(Long id) {
        return entityFinder.findClient(id);
    }

    public List<ClientEntity> getAllClients() {
        List<ClientEntity> clientEntities = new ArrayList<>();
        clientRepository.findAll().forEach(clientEntities::add);
        return clientEntities;
    }

    public ClientEntity createClient(ClientEntity clientEntity) {
        if (clientRepository.existsByEmail(clientEntity.getEmail())) {
            throw new IllegalArgumentException("Client with this email already exists");
        }
        return clientRepository.save(clientEntity);
    }

    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }

    public DealEntity assignDealToClient(Long clientId, Long dealId) {
        ClientEntity foundClientEntity = entityFinder.findClient(clientId);
        DealEntity foundDealEntity = entityFinder.findDeal(dealId);

        List<DealEntity> dealsOfClient = foundClientEntity.getDeals();
        dealsOfClient.add(foundDealEntity);
        foundClientEntity.setDeals(dealsOfClient);
        clientRepository.save(foundClientEntity);

        foundDealEntity.setClient(foundClientEntity);
        dealRepository.save(foundDealEntity);

        return foundDealEntity;
    }

    public void removeDealFromClient(Long clientId, Long dealId) {
        ClientEntity foundClientEntity = entityFinder.findClient(clientId);
        DealEntity foundDealEntity = entityFinder.findDeal(dealId);

        List<DealEntity> dealsClient = foundClientEntity.getDeals();
        dealsClient.remove(foundDealEntity);
        foundClientEntity.setDeals(dealsClient);
        clientRepository.save(foundClientEntity);

        foundDealEntity.setClient(null);
        dealRepository.save(foundDealEntity);
    }
}
