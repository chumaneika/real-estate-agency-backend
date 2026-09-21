package com.petproject.term_paper.service;

import com.petproject.term_paper.dto.DealDTO;
import com.petproject.term_paper.dto.DealRequest;
import com.petproject.term_paper.entity.DealEntity;
import com.petproject.term_paper.entity.UserEntity;
import com.petproject.term_paper.entity.enums.UserRole;
import com.petproject.term_paper.repository.DealRepository;
import com.petproject.term_paper.util.EntityFinder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class DealService {
    private final DealRepository deals;
    private final EntityFinder finder;

    @Transactional(readOnly = true)
    public DealDTO getDealById(Long id) { return toDTO(finder.findDeal(id)); }

    @Transactional(readOnly = true)
    public List<DealDTO> getAllDeals() { return deals.findAll().stream().map(this::toDTO).toList(); }

    public DealDTO createDeal(DealRequest request) {
        if (request == null) throw new IllegalArgumentException("Deal data is required.");
        DealEntity deal = new DealEntity();
        apply(deal, request);
        return toDTO(deals.save(deal));
    }

    @Transactional
    public DealDTO updateDeal(Long id, DealRequest request) {
        if (request == null) throw new IllegalArgumentException("Deal data is required.");
        DealEntity deal = finder.findDeal(id);
        apply(deal, request);
        return toDTO(deal);
    }

    private void apply(DealEntity deal, DealRequest request) {
        if (request.getTitle() == null || request.getTitle().isBlank() || request.getDateOpen() == null || request.getStatus() == null) {
            throw new IllegalArgumentException("Title, opening date and status are required.");
        }
        deal.setTitle(request.getTitle().trim());
        deal.setDateOpen(request.getDateOpen());
        deal.setDateClose(request.getDateClose());
        deal.setStatus(request.getStatus());
        deal.setPrice(request.getPrice());
        deal.setProperty(request.getPropertyId() == null ? null : finder.findProperty(request.getPropertyId()));
        deal.setClient(resolveUser(request.getClientId(), UserRole.CLIENT, "client"));
        deal.setAgent(resolveUser(request.getAgentId(), UserRole.AGENT, "agent"));
    }

    public void deleteDeal(Long id) { deals.delete(finder.findDeal(id)); }

    private UserEntity resolveUser(Long id, UserRole role, String label) {
        if (id == null) return null;
        UserEntity user = finder.findUser(id);
        if (user.getRole() != role) throw new IllegalArgumentException("Selected " + label + " has an invalid role.");
        return user;
    }

    private DealDTO toDTO(DealEntity deal) {
        return new DealDTO(deal.getId(), deal.getTitle(), deal.getDateOpen(), deal.getDateClose(), deal.getStatus(),
                deal.getPrice(), id(deal.getProperty()), id(deal.getClient()), username(deal.getClient()),
                id(deal.getAgent()), username(deal.getAgent()), deal.getLegacyClientId(), deal.getLegacyEmployeeId());
    }

    private Long id(com.petproject.term_paper.entity.PropertyEntity value) { return value == null ? null : value.getId(); }
    private Long id(UserEntity value) { return value == null ? null : value.getId(); }
    private String username(UserEntity value) { return value == null ? null : value.getUsername(); }
}
