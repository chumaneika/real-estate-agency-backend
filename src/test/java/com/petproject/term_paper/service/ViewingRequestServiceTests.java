package com.petproject.term_paper.service;

import com.petproject.term_paper.dto.CreateViewingRequest;
import com.petproject.term_paper.entity.PropertyEntity;
import com.petproject.term_paper.entity.UserEntity;
import com.petproject.term_paper.entity.ViewingRequestEntity;
import com.petproject.term_paper.entity.enums.ViewingRequestStatus;
import com.petproject.term_paper.entity.enums.UserRole;
import com.petproject.term_paper.repository.ViewingRequestRepository;
import com.petproject.term_paper.util.EntityFinder;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ViewingRequestServiceTests {
    private final ViewingRequestRepository repository = mock(ViewingRequestRepository.class);
    private final UserDetailsServiceImpl users = mock(UserDetailsServiceImpl.class);
    private final EntityFinder finder = mock(EntityFinder.class);
    private final ViewingRequestService service = new ViewingRequestService(repository, users, finder);

    @Test
    void createsPendingRequestForCurrentUser() {
        UserEntity user = new UserEntity();
        user.setId(4L);
        user.setUsername("malik9");
        user.setRole(UserRole.CLIENT);
        user.setEnabled(true);
        PropertyEntity property = new PropertyEntity();
        property.setId(7L);
        property.setTitle("Garden home");
        CreateViewingRequest request = validRequest();
        request.setComment("  Please call before the viewing.  ");
        when(users.findByUsername("malik9")).thenReturn(Optional.of(user));
        when(finder.findProperty(7L)).thenReturn(property);
        when(repository.save(any(ViewingRequestEntity.class))).thenAnswer(invocation -> {
            ViewingRequestEntity entity = invocation.getArgument(0);
            entity.setId(12L);
            return entity;
        });

        var result = service.create("malik9", request);
        assertEquals(12L, result.id());
        assertEquals(7L, result.propertyId());
        assertEquals("malik9", result.username());
        assertEquals("Please call before the viewing.", result.comment());
        assertEquals(ViewingRequestStatus.PENDING, result.status());
        assertNotNull(result.createdAt());
    }

    @Test
    void rejectsPastViewing() {
        CreateViewingRequest request = validRequest();
        request.setViewingDate(LocalDate.now().minusDays(1));
        assertThrows(IllegalArgumentException.class, () -> service.create("malik9", request));
        verifyNoInteractions(repository, users, finder);
    }

    @Test
    void rejectsTimeOutsideHalfHourSchedule() {
        CreateViewingRequest request = validRequest();
        request.setViewingTime(LocalTime.of(10, 15));
        assertThrows(IllegalArgumentException.class, () -> service.create("malik9", request));
        verifyNoInteractions(repository, users, finder);
    }

    @Test
    void rejectsLongComment() {
        CreateViewingRequest request = validRequest();
        request.setComment("a".repeat(1001));
        assertThrows(IllegalArgumentException.class, () -> service.create("malik9", request));
    }

    @Test
    void rejectsViewingRequestFromAgentAccount() {
        UserEntity agent = new UserEntity();
        agent.setUsername("agent");
        agent.setRole(UserRole.AGENT);
        agent.setEnabled(true);
        when(users.findByUsername("agent")).thenReturn(Optional.of(agent));

        assertThrows(IllegalArgumentException.class, () -> service.create("agent", validRequest()));
        verifyNoInteractions(repository, finder);
    }

    private CreateViewingRequest validRequest() {
        CreateViewingRequest request = new CreateViewingRequest();
        request.setPropertyId(7L);
        request.setViewingDate(LocalDate.now().plusDays(2));
        request.setViewingTime(LocalTime.of(10, 0));
        return request;
    }
}
