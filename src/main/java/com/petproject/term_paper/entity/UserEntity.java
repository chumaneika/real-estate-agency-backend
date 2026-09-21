package com.petproject.term_paper.entity;

import com.petproject.term_paper.entity.enums.UserRole;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "username", unique = true, nullable = false)
    private String username;
    @Column(name = "email", unique = true, nullable = false)
    private String email;
    @Column(name = "password", nullable = false)
    private String password;

    // The legacy column is retained so existing installations can upgrade in place.
    @Convert(converter = UserRoleConverter.class)
    @Column(name = "roles", nullable = false)
    private UserRole role = UserRole.CLIENT;

    @Column(name = "enabled", nullable = false)
    private boolean enabled = true;

    @OneToMany(mappedBy = "agent")
    private List<PropertyEntity> assignedProperties = new ArrayList<>();
    @OneToMany(mappedBy = "client")
    private List<DealEntity> clientDeals = new ArrayList<>();
    @OneToMany(mappedBy = "agent")
    private List<DealEntity> agentDeals = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public List<PropertyEntity> getAssignedProperties() { return assignedProperties; }
    public void setAssignedProperties(List<PropertyEntity> assignedProperties) { this.assignedProperties = assignedProperties; }
    public List<DealEntity> getClientDeals() { return clientDeals; }
    public void setClientDeals(List<DealEntity> clientDeals) { this.clientDeals = clientDeals; }
    public List<DealEntity> getAgentDeals() { return agentDeals; }
    public void setAgentDeals(List<DealEntity> agentDeals) { this.agentDeals = agentDeals; }
}
