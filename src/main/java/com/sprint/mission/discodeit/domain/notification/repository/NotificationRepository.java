package com.sprint.mission.discodeit.domain.notification.repository;

import com.sprint.mission.discodeit.domain.notification.entity.Notification;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {

  List<Notification> findAllByReceiverId(UUID userId);

}
