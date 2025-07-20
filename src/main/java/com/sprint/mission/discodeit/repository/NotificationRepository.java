package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.domain.Notification;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {

  List<Notification> findByUserId(UUID userId);

  boolean existsByIdAndUserId(UUID id, UUID userId);

}
