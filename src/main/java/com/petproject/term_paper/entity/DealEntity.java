package com.petproject.term_paper.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "deals")
public class DealEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "dateOpen", nullable = false)
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateOpen;

    @Column(name = "dateClose", nullable = true)
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateClose;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusDeal status;

    @Column(name = "price")
    private Double price;

    @ManyToOne
    @JoinColumn(name = "property_id")
    @JsonBackReference(value = "owner-properties")
    private PropertyEntity propertyEntity;

    @ManyToOne
    @JoinColumn(name = "client_id")
    @JsonBackReference(value = "client-deals")
    private ClientEntity clientEntity;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    @JsonBackReference(value = "employee-deals")
    private EmployeeEntity employeeEntity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getDateOpen() {
        return dateOpen;
    }

    public void setDateOpen(LocalDate dateOpen) {
        this.dateOpen = dateOpen;
    }

    public LocalDate getDateClose() {
        return dateClose;
    }

    public void setDateClose(LocalDate dateClose) {
        this.dateClose = dateClose;
    }

    public StatusDeal getStatus() {
        return status;
    }

    public void setStatus(StatusDeal status) {
        this.status = status;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public PropertyEntity getProperty() {
        return propertyEntity;
    }

    public void setProperty(PropertyEntity propertyEntity) {
        this.propertyEntity = propertyEntity;
    }

    public ClientEntity getClient() {
        return clientEntity;
    }

    public void setClient(ClientEntity clientEntity) {
        this.clientEntity = clientEntity;
    }

    public EmployeeEntity getEmployee() {
        return employeeEntity;
    }

    public void setEmployee(EmployeeEntity employeeEntity) {
        this.employeeEntity = employeeEntity;
    }
}

