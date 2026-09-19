package com.petproject.term_paper.service;

import com.petproject.term_paper.entity.ViewingRequestStatus;
import com.petproject.term_paper.repository.DealRepository;
import com.petproject.term_paper.repository.PropertyRepository;
import com.petproject.term_paper.repository.UserRepository;
import com.petproject.term_paper.repository.ViewingRequestRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AdminDashboardServiceTests {
    private final PropertyRepository properties = mock(PropertyRepository.class);
    private final UserRepository users = mock(UserRepository.class);
    private final DealRepository deals = mock(DealRepository.class);
    private final ViewingRequestRepository viewingRequests = mock(ViewingRequestRepository.class);
    private final AdminDashboardService service = new AdminDashboardService(properties, users, deals, viewingRequests);

    @Test
    void returnsCountsRecentRequestsAndSevenActivityPoints() {
        when(properties.count()).thenReturn(12L);
        when(users.count()).thenReturn(7L);
        when(deals.count()).thenReturn(4L);
        when(viewingRequests.countByStatus(ViewingRequestStatus.PENDING)).thenReturn(3L);
        when(viewingRequests.findTop5ByOrderByCreatedAtDesc()).thenReturn(List.of());

        var dashboard = service.getDashboard();

        assertEquals(12L, dashboard.propertyCount());
        assertEquals(7L, dashboard.userCount());
        assertEquals(4L, dashboard.dealCount());
        assertEquals(3L, dashboard.pendingRequestCount());
        assertEquals(7, dashboard.activity().size());
        verify(deals, times(7)).countByDateOpen(any());
        verify(viewingRequests, times(7)).countByCreatedAtGreaterThanEqualAndCreatedAtLessThan(any(), any());
    }
}
