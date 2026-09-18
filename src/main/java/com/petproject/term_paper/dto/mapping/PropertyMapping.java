package com.petproject.term_paper.dto.mapping;

import com.petproject.term_paper.dto.PropertyDTO;
import com.petproject.term_paper.entity.PropertyEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PropertyMapping {
    PropertyDTO toDTO(PropertyEntity property);
}
