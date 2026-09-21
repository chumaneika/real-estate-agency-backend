package com.petproject.term_paper.entity;

import com.petproject.term_paper.entity.enums.PropertyType;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "properties")
public class PropertyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title") private String title;
    @Column(name = "description") private String description;
    @Column(name = "address", nullable = false) private String address;
    @Column(name = "area") private Double area;
    @Column(name = "rooms") private Integer rooms;
    @Column(name = "price") private Double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private OwnerEntity owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id")
    private UserEntity agent;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private PropertyType type;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "property_images", joinColumns = @JoinColumn(name = "property_id"))
    @OrderColumn(name = "display_order")
    @Column(name = "image_url", nullable = false, length = 2048)
    private List<String> imageUrls = new ArrayList<>();

    @OneToMany(mappedBy = "property")
    private List<DealEntity> deals = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public Double getArea() { return area; }
    public void setArea(Double area) { this.area = area; }
    public Integer getRooms() { return rooms; }
    public void setRooms(Integer rooms) { this.rooms = rooms; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public OwnerEntity getOwner() { return owner; }
    public void setOwner(OwnerEntity owner) { this.owner = owner; }
    public UserEntity getAgent() { return agent; }
    public void setAgent(UserEntity agent) { this.agent = agent; }
    public PropertyType getType() { return type; }
    public void setType(PropertyType type) { this.type = type; }
    public List<String> getImageUrls() { return imageUrls; }
    public void setImageUrls(List<String> imageUrls) { this.imageUrls = imageUrls; }
    public List<DealEntity> getDeals() { return deals; }
    public void setDeals(List<DealEntity> deals) { this.deals = deals; }
}
