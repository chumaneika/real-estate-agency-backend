package com.petproject.term_paper.service;

import com.petproject.term_paper.dto.CreateViewingRequest;
import com.petproject.term_paper.dto.ViewingRequestDTO;
import com.petproject.term_paper.entity.PropertyEntity;
import com.petproject.term_paper.entity.UserEntity;
import com.petproject.term_paper.entity.ViewingRequestEntity;
import com.petproject.term_paper.entity.ViewingRequestStatus;
import com.petproject.term_paper.repository.ViewingRequestRepository;
import com.petproject.term_paper.util.EntityFinder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@AllArgsConstructor
public class ViewingRequestService {
    private static final LocalTime OPENING_TIME = LocalTime.of(9, 0);
    private static final LocalTime LAST_VIEWING_TIME = LocalTime.of(18, 30);

    private final ViewingRequestRepository viewingRequests;
    private final UserDetailsServiceImpl users;
    private final EntityFinder entityFinder;

    public ViewingRequestDTO create(String username, CreateViewingRequest request) {
        validate(request);
        UserEntity user = users.findByUsername(username)
                .filter(UserEntity::isEnabled)
                .orElseThrow(() -> new IllegalArgumentException("Your account is unavailable."));
        PropertyEntity property = entityFinder.findProperty(request.getPropertyId());

        ViewingRequestEntity entity = new ViewingRequestEntity();
        entity.setProperty(property);
        entity.setUser(user);
        entity.setViewingDate(request.getViewingDate());
        entity.setViewingTime(request.getViewingTime().withSecond(0).withNano(0));
        entity.setComment(normalizeComment(request.getComment()));
        entity.setStatus(ViewingRequestStatus.PENDING);
        entity.setCreatedAt(LocalDateTime.now());
        return toDTO(viewingRequests.save(entity));
    }

    private void validate(CreateViewingRequest request) {
        if (request == null || request.getPropertyId() == null || request.getPropertyId() <= 0
                || request.getViewingDate() == null || request.getViewingTime() == null) {
            throw new IllegalArgumentException("Property, date and time are required.");
        }
        LocalTime time = request.getViewingTime();
        if (time.isBefore(OPENING_TIME) || time.isAfter(LAST_VIEWING_TIME)
                || time.getMinute() % 30 != 0 || time.getSecond() != 0 || time.getNano() != 0) {
            throw new IllegalArgumentException("Choose a viewing time between 09:00 and 18:30 in 30-minute intervals.");
        }
        if (!LocalDateTime.of(request.getViewingDate(), time).isAfter(LocalDateTime.now().plusMinutes(30))) {
            throw new IllegalArgumentException("Choose a future date and time.");
        }
        if (request.getComment() != null && request.getComment().trim().length() > 1000) {
            throw new IllegalArgumentException("Comment must contain no more than 1000 characters.");
        }
    }

    private String normalizeComment(String comment) {
        if (comment == null || comment.isBlank()) return null;
        return comment.trim();
    }

    private ViewingRequestDTO toDTO(ViewingRequestEntity entity) {
        return new ViewingRequestDTO(
                entity.getId(), entity.getProperty().getId(), entity.getProperty().getTitle(),
                entity.getUser().getUsername(), entity.getViewingDate(), entity.getViewingTime(),
                entity.getComment(), entity.getStatus(), entity.getCreatedAt()
        );
    }
}
