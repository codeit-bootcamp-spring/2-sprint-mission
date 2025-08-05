package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Notification;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    List<Notification> findAllByReceiverIdOrderByCreatedAtDesc(UUID receiverId);
    void deleteByIdAndReceiverId(UUID id, UUID receiverId);
    List<Notification> findByReceiverIdAndCreatedAtAfterOrderByCreatedAtAsc(UUID receiverId,
        Instant createdAt);
} 