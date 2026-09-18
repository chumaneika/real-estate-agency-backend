package com.petproject.term_paper.dto;

import com.petproject.term_paper.entity.PropertyType;
import lombok.Data;

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
}
