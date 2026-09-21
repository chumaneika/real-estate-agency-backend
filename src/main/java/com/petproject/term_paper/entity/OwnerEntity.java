package com.petproject.term_paper.entity;

import com.petproject.term_paper.entity.enums.OwnerType;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "owners")
public class OwnerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "owner_type", nullable = false, columnDefinition = "varchar(32) default 'INDIVIDUAL'")
    private OwnerType ownerType = OwnerType.INDIVIDUAL;
    @Column(name = "phoneNumber") private String phone;
    @Column(name = "email") private String email;
    @Column(name = "name") private String firstName;
    @Column(name = "surname") private String lastName;
    @Column(name = "middle_name") private String middleName;
    @Column(name = "company_name") private String companyName;
    @Column(name = "tax_id") private String taxId;
    @Column(name = "registration_number") private String registrationNumber;

    @OneToMany(mappedBy = "owner")
    private List<PropertyEntity> properties = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public OwnerType getOwnerType() { return ownerType; }
    public void setOwnerType(OwnerType ownerType) { this.ownerType = ownerType; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getMiddleName() { return middleName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getTaxId() { return taxId; }
    public void setTaxId(String taxId) { this.taxId = taxId; }
    public String getRegistrationNumber() { return registrationNumber; }
    public void setRegistrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; }
    public List<PropertyEntity> getProperties() { return properties; }
    public void setProperties(List<PropertyEntity> properties) { this.properties = properties; }
}
