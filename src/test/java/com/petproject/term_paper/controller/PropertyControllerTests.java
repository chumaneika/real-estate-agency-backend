package com.petproject.term_paper.controller;

import com.petproject.term_paper.dto.PropertyDTO;
import com.petproject.term_paper.dto.mapping.PropertyMapping;
import com.petproject.term_paper.entity.PropertyEntity;
import com.petproject.term_paper.service.PropertyService;
import jakarta.persistence.EntityNotFoundException;
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
        assertEquals(List.of(dto), controller.getAllProperties().getBody());
    }

    @Test
    void detailReturnsOkInsteadOfRedirect() {
        PropertyEntity property = new PropertyEntity();
        PropertyDTO dto = new PropertyDTO();
        when(service.getPropertyById(1L)).thenReturn(property);
        when(mapping.toDTO(property)).thenReturn(dto);
        var response = controller.getPropertyById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
    }

    @Test
    void missingPropertyReturnsNotFound() {
        when(service.getPropertyById(1L)).thenThrow(new EntityNotFoundException());
        assertEquals(HttpStatus.NOT_FOUND, controller.getPropertyById(1L).getStatusCode());
    }

    @Test
    void createReturnsPropertyWithCloudinaryImageUrls() {
        PropertyEntity request = new PropertyEntity();
        request.setImageUrls(List.of(
                "https://res.cloudinary.com/demo/image/upload/v1/first.jpg",
                "https://res.cloudinary.com/demo/image/upload/v1/second.jpg"
        ));
        PropertyEntity saved = new PropertyEntity();
        saved.setId(1L);
        saved.setImageUrls(request.getImageUrls());
        PropertyDTO dto = new PropertyDTO();
        dto.setId(1L);
        dto.setImageUrls(saved.getImageUrls());

        when(service.createProperty(request)).thenReturn(saved);
        when(mapping.toDTO(saved)).thenReturn(dto);

        var response = controller.createProperty(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(dto, response.getBody());
        assertEquals(saved.getImageUrls(), response.getBody().getImageUrls());
    }
}
