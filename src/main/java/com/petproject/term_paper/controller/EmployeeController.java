package com.petproject.term_paper.controller;

import com.petproject.term_paper.entity.DealEntity;
import com.petproject.term_paper.entity.EmployeeEntity;
import com.petproject.term_paper.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
@AllArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping("/get-all")
    public ResponseEntity<List<EmployeeEntity>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeEntity> getOwnerById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<EmployeeEntity> createEmployee(@RequestBody EmployeeEntity employeeEntity) {
        EmployeeEntity createdEmployeeEntity = employeeService.createEmployee(employeeEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEmployeeEntity);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable("id") Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update-name")
    public ResponseEntity<Void> updateName(@RequestParam Long id, @RequestParam String name) {
        employeeService.updateName(id, name);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update-position")
    public ResponseEntity<Void> updatePosition(@RequestParam Long id, @RequestParam String position) {
        employeeService.updatePosition(id, position);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/assign-deal/{employeeId}/{dealId}")
    public ResponseEntity<DealEntity> addDealToEmployee(@PathVariable("employeeId") Long employeeId, @PathVariable("dealId") Long dealId) {
        DealEntity addedDealEntity = employeeService.addDealToEmployee(employeeId, dealId);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedDealEntity);

        // todo "come back late"
    }

    @DeleteMapping("/delete-property/{employeeId}/{dealId}")
    public ResponseEntity<Void> deletePropertyFromOwner(@PathVariable("employeeId") Long employeeId, @PathVariable("dealId") Long dealId) {
        employeeService.removeDealFromEmployee(employeeId, dealId);
        return ResponseEntity.noContent().build();

        // todo "come back late"
    }
}
