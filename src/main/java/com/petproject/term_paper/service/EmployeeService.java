package com.petproject.term_paper.service;

import com.petproject.term_paper.entity.DealEntity;
import com.petproject.term_paper.entity.EmployeeEntity;
import com.petproject.term_paper.repository.DealRepository;
import com.petproject.term_paper.repository.EmployeeRepository;
import com.petproject.term_paper.util.EntityFinder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final DealRepository dealRepository;
    private final EntityFinder entityFinder;

    public List<EmployeeEntity> getAllEmployees() {
        List<EmployeeEntity> employeeEntities = new ArrayList<>();
        employeeRepository.findAll().forEach(employeeEntities::add);
        return employeeEntities;
    }

    public EmployeeEntity getEmployeeById(Long id) {
        return entityFinder.findEmployee(id);
    }

    public EmployeeEntity createEmployee(EmployeeEntity employeeEntity) {
        if (employeeRepository.existsByName(employeeEntity.getName())) {
            throw new IllegalArgumentException("Employee with this name already exists");
        }

        return employeeRepository.save(employeeEntity);
    }

    public void deleteEmployee(Long id) {
        EmployeeEntity foundEmployeeEntity = entityFinder.findEmployee(id);

        for (DealEntity dealEntity : foundEmployeeEntity.getDealEntities()) {
            dealEntity.setEmployee(null);
            dealRepository.save(dealEntity);
        }

        employeeRepository.deleteById(id);
    }

    public void updateName(Long employeeId, String name) {
        EmployeeEntity employeeEntity = entityFinder.findEmployee(employeeId);

        employeeEntity.setName(name);

        employeeRepository.save(employeeEntity);
    }

    public EmployeeEntity updatePosition(Long employeeId, String position) {
        EmployeeEntity employeeEntity = entityFinder.findEmployee(employeeId);

        employeeEntity.setPosition(position);

        return employeeRepository.save(employeeEntity);
    }

    public DealEntity addDealToEmployee(Long employeeId, Long dealId) {
        EmployeeEntity employeeEntity = entityFinder.findEmployee(employeeId);
        DealEntity dealEntity = entityFinder.findDeal(dealId);
        List<DealEntity> dealsOfEmployee = employeeEntity.getDealEntities();

        dealsOfEmployee.add(dealEntity);
        employeeEntity.setDealEntities(dealsOfEmployee);
        dealEntity.setEmployee(employeeEntity);

        employeeRepository.save(employeeEntity);

        return dealRepository.save(dealEntity);
    }

    public DealEntity removeDealFromEmployee(Long employeeId, Long dealId) {
        EmployeeEntity employeeEntity = entityFinder.findEmployee(employeeId);
        DealEntity dealEntity = entityFinder.findDeal(dealId);
        List<DealEntity> dealsOfEmployee = employeeEntity.getDealEntities();

        dealsOfEmployee.remove(dealEntity);
        employeeEntity.setDealEntities(dealsOfEmployee);
        dealEntity.setEmployee(null);

        employeeRepository.save(employeeEntity);

        return dealRepository.save(dealEntity);
    }
}
