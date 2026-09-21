package com.petproject.term_paper.controller;

import com.petproject.term_paper.dto.PropertyCreateRequest;
import com.petproject.term_paper.dto.PropertyDTO;
import com.petproject.term_paper.dto.mapping.PropertyMapping;
import com.petproject.term_paper.entity.PropertyEntity;
import com.petproject.term_paper.service.PropertyService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PropertyControllerTests {
    private final PropertyService service = mock(PropertyService.class);
    private final PropertyMapping mapping = mock(PropertyMapping.class);
    private final PropertyController controller = new PropertyController(service, mapping);

    @Test
    void returnsFlatCatalogData() {
        PropertyEntity property = new PropertyEntity();
        PropertyDTO dto = new PropertyDTO();
        dto.setId(1L);
        when(service.getAllProperties()).thenReturn(List.of(property));
        when(mapping.toDTO(property)).thenReturn(dto);
        assertEquals(List.of(dto), controller.getAllProperties());
    }

    @Test
    void detailReturnsDto() {
        PropertyEntity property = new PropertyEntity();
        PropertyDTO dto = new PropertyDTO();
        when(service.getPropertyById(1L)).thenReturn(property);
        when(mapping.toDTO(property)).thenReturn(dto);
        assertEquals(dto, controller.getPropertyById(1L));
    }

    @Test
    void createAcceptsRelationshipIdsThroughRequestDto() {
        PropertyCreateRequest request = new PropertyCreateRequest();
        request.setOwnerId(8L);
        PropertyEntity saved = new PropertyEntity();
        saved.setId(1L);
        PropertyDTO dto = new PropertyDTO();
        dto.setId(1L);
        when(service.createProperty(request)).thenReturn(saved);
        when(mapping.toDTO(saved)).thenReturn(dto);

        var response = controller.createProperty(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(dto, response.getBody());
    }
}
