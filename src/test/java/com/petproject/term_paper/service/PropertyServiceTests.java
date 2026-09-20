package com.petproject.term_paper.service;

import com.petproject.term_paper.entity.PropertyEntity;
import com.petproject.term_paper.repository.OwnerRepository;
import com.petproject.term_paper.repository.PropertyRepository;
import com.petproject.term_paper.util.EntityFinder;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PropertyServiceTests {
    private final PropertyRepository propertyRepository = mock(PropertyRepository.class);
    private final PropertyService service = new PropertyService(
            propertyRepository,
            mock(OwnerRepository.class),
            mock(EntityFinder.class)
    );

    @Test
    void createPropertyStoresCloudinaryUrlsInTheirOriginalOrder() {
        PropertyEntity property = new PropertyEntity();
        property.setImageUrls(List.of(
                " https://res.cloudinary.com/demo/image/upload/v1/front.jpg ",
                "https://res.cloudinary.com/demo/image/upload/v1/kitchen.jpg"
        ));
        when(propertyRepository.save(property)).thenReturn(property);

        service.createProperty(property);

        assertEquals(List.of(
                "https://res.cloudinary.com/demo/image/upload/v1/front.jpg",
                "https://res.cloudinary.com/demo/image/upload/v1/kitchen.jpg"
        ), property.getImageUrls());
        verify(propertyRepository).save(property);
    }

    @Test
    void createPropertyRejectsBlankImageUrl() {
        PropertyEntity property = new PropertyEntity();
        property.setImageUrls(List.of(" "));

        assertThrows(IllegalArgumentException.class, () -> service.createProperty(property));
    }
}
