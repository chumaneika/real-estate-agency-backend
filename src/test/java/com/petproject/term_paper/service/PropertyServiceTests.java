package com.petproject.term_paper.service;

import com.petproject.term_paper.dto.PropertyCreateRequest;
import com.petproject.term_paper.entity.OwnerEntity;
import com.petproject.term_paper.entity.PropertyEntity;
import com.petproject.term_paper.entity.UserEntity;
import com.petproject.term_paper.entity.enums.UserRole;
import com.petproject.term_paper.repository.OwnerRepository;
import com.petproject.term_paper.repository.PropertyRepository;
import com.petproject.term_paper.repository.UserRepository;
import com.petproject.term_paper.repository.ViewingRequestRepository;
import com.petproject.term_paper.repository.DealRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PropertyServiceTests {
    private final PropertyRepository properties = mock(PropertyRepository.class);
    private final OwnerRepository owners = mock(OwnerRepository.class);
    private final UserRepository users = mock(UserRepository.class);
    private final ViewingRequestRepository viewingRequests = mock(ViewingRequestRepository.class);
    private final DealRepository deals = mock(DealRepository.class);
    private final PropertyService service = new PropertyService(properties, owners, users, viewingRequests, deals);

    @Test
    void createPropertyResolvesOwnerAndAgentAndNormalizesImages() {
        OwnerEntity owner = new OwnerEntity();
        owner.setId(3L);
        UserEntity agent = new UserEntity();
        agent.setId(4L);
        agent.setRole(UserRole.AGENT);
        agent.setEnabled(true);
        when(owners.findById(3L)).thenReturn(Optional.of(owner));
        when(users.findById(4L)).thenReturn(Optional.of(agent));
        when(properties.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        PropertyCreateRequest request = request();
        request.setAgentId(4L);

        PropertyEntity result = service.createProperty(request);

        assertEquals(owner, result.getOwner());
        assertEquals(agent, result.getAgent());
        assertEquals(List.of("https://images.example/front.jpg"), result.getImageUrls());
    }

    @Test
    void rejectsAClientAssignedAsAgent() {
        UserEntity client = new UserEntity();
        client.setRole(UserRole.CLIENT);
        when(owners.findById(3L)).thenReturn(Optional.of(new OwnerEntity()));
        when(users.findById(4L)).thenReturn(Optional.of(client));
        PropertyCreateRequest request = request();
        request.setAgentId(4L);

        assertThrows(IllegalArgumentException.class, () -> service.createProperty(request));
        verify(properties, never()).save(any());
    }

    @Test
    void rejectsBlankImageUrl() {
        when(owners.findById(3L)).thenReturn(Optional.of(new OwnerEntity()));
        PropertyCreateRequest request = request();
        request.setImageUrls(List.of(" "));
        assertThrows(IllegalArgumentException.class, () -> service.createProperty(request));
    }

    @Test
    void rejectsDeletingPropertyWithViewingRequests() {
        when(properties.existsById(9L)).thenReturn(true);
        when(viewingRequests.existsByPropertyId(9L)).thenReturn(true);

        assertThrows(PropertyService.PropertyInUseException.class, () -> service.deleteProperty(9L));
        verify(properties, never()).deleteById(anyLong());
    }

    private PropertyCreateRequest request() {
        PropertyCreateRequest request = new PropertyCreateRequest();
        request.setAddress("Main street");
        request.setOwnerId(3L);
        request.setImageUrls(List.of(" https://images.example/front.jpg "));
        return request;
    }
}
