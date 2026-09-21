package com.petproject.term_paper.entity;

import com.petproject.term_paper.entity.enums.StatusDeal;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "deals")
public class DealEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title", nullable = false) private String title;
    @Column(name = "dateOpen", nullable = false) private LocalDate dateOpen;
    @Column(name = "dateClose") private LocalDate dateClose;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false) private StatusDeal status;
    @Column(name = "price") private Double price;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id") private PropertyEntity property;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_user_id") private UserEntity client;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_user_id") private UserEntity agent;

    // Read-only references keep legacy deal links auditable until an administrator
    // can match the old client/employee rows to real user accounts.
    @Column(name = "client_id", insertable = false, updatable = false)
    private Long legacyClientId;
    @Column(name = "employee_id", insertable = false, updatable = false)
    private Long legacyEmployeeId;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public LocalDate getDateOpen() { return dateOpen; }
    public void setDateOpen(LocalDate dateOpen) { this.dateOpen = dateOpen; }
    public LocalDate getDateClose() { return dateClose; }
    public void setDateClose(LocalDate dateClose) { this.dateClose = dateClose; }
    public StatusDeal getStatus() { return status; }
    public void setStatus(StatusDeal status) { this.status = status; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public PropertyEntity getProperty() { return property; }
    public void setProperty(PropertyEntity property) { this.property = property; }
    public UserEntity getClient() { return client; }
    public void setClient(UserEntity client) { this.client = client; }
    public UserEntity getAgent() { return agent; }
    public void setAgent(UserEntity agent) { this.agent = agent; }
    public Long getLegacyClientId() { return legacyClientId; }
    public Long getLegacyEmployeeId() { return legacyEmployeeId; }
}
