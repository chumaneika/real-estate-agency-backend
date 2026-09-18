package com.petproject.term_paper.service;

import com.petproject.term_paper.entity.ClientEntity;
import com.petproject.term_paper.entity.DealEntity;
import com.petproject.term_paper.entity.EmployeeEntity;
import com.petproject.term_paper.entity.PropertyEntity;
import com.petproject.term_paper.repository.ClientRepository;
import com.petproject.term_paper.repository.DealRepository;
import com.petproject.term_paper.repository.EmployeeRepository;
import com.petproject.term_paper.repository.PropertyRepository;
import com.petproject.term_paper.util.EntityFinder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class DealService {
    private final DealRepository dealRepository;
    private final EmployeeRepository employeeRepository;
    private final ClientRepository clientRepository;
    private final PropertyRepository propertyRepository;
    private final EntityFinder entityFinder;

    public DealEntity getDealById(Long id) {
        return entityFinder.findDeal(id);
    }

    public List<DealEntity> getAllDeals() {
        List<DealEntity> dealEntities = new ArrayList<>();
        dealRepository.findAll().forEach(dealEntities::add);
        return dealEntities;
    }

    public DealEntity createDeal(DealEntity dealEntity) {
        return dealRepository.save(dealEntity);
    }

    public void deleteDeal(Long dealId) {
        DealEntity dealEntity = entityFinder.findDeal(dealId);

        if (dealEntity.getEmployee() != null) {
            EmployeeEntity employeeEntity = dealEntity.getEmployee();
            employeeEntity.getDealEntities().remove(dealEntity);
            dealEntity.setEmployee(null);
        }

        if (dealEntity.getClient() != null) {
            ClientEntity clientEntity = dealEntity.getClient();
            clientEntity.getDeals().remove(dealEntity);
            dealEntity.setClient(null);
        }

        if (dealEntity.getProperty() != null) {
            PropertyEntity propertyEntity = dealEntity.getProperty();
            propertyEntity.getDealEntities().remove(dealEntity);
            dealEntity.setProperty(null);
        }

        dealRepository.save(dealEntity);

        dealRepository.delete(dealEntity);
    }
}
