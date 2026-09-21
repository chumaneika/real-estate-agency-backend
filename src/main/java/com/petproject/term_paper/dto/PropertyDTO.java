package com.petproject.term_paper.dto;

import com.petproject.term_paper.entity.enums.PropertyType;
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
    private PropertyType type;
    private List<String> imageUrls;
}
