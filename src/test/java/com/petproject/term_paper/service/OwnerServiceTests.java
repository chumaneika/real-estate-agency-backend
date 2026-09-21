package com.petproject.term_paper.service;

import com.petproject.term_paper.dto.OwnerCreateRequest;
import com.petproject.term_paper.entity.enums.OwnerType;
import com.petproject.term_paper.repository.OwnerRepository;
import com.petproject.term_paper.repository.PropertyRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OwnerServiceTests {
    private final OwnerRepository owners = mock(OwnerRepository.class);
    private final PropertyRepository properties = mock(PropertyRepository.class);
    private final OwnerService service = new OwnerService(owners, properties);

    @Test
    void createsValidatedIndividualOwner() {
        OwnerCreateRequest request = new OwnerCreateRequest();
        request.setOwnerType(OwnerType.INDIVIDUAL);
        request.setFirstName("  Malik ");
        request.setLastName(" Aliev ");
        request.setEmail(" OWNER@EXAMPLE.COM ");
        when(owners.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var result = service.createOwner(request);

        assertEquals("Malik", result.firstName());
        assertEquals("Aliev", result.lastName());
        assertEquals("owner@example.com", result.email());
    }

    @Test
    void companyRequiresAName() {
        OwnerCreateRequest request = new OwnerCreateRequest();
        request.setOwnerType(OwnerType.COMPANY);
        assertThrows(IllegalArgumentException.class, () -> service.createOwner(request));
        verify(owners, never()).save(any());
    }

    @Test
    void ownerWithPropertiesCannotBeDeleted() {
        when(owners.existsById(9L)).thenReturn(true);
        when(properties.existsByOwnerId(9L)).thenReturn(true);
        assertThrows(OwnerService.OwnerInUseException.class, () -> service.deleteOwner(9L));
        verify(owners, never()).deleteById(any());
    }
}
