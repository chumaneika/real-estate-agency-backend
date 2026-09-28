package com.petproject.term_paper.dto;

import com.petproject.term_paper.entity.enums.PropertyType;
import com.petproject.term_paper.entity.enums.RenovationType;
import lombok.Data;

import java.util.List;

@Data
public class PropertyDTO {
    private Long id;
    private String title;
    private String description;
    private String address;
    private Double area;
    private Integer rooms;
    private Double price;
    private Integer floor;
    private Integer totalFloors;
    private Integer constructionYear;
    private RenovationType renovation;
    private Boolean hasBalcony;
    private Boolean hasParking;
    private Integer metroDistanceMinutes;
    private PropertyType type;
    private List<String> imageUrls;
    private OwnerSummaryDTO owner;
    private AgentSummaryDTO agent;
}
