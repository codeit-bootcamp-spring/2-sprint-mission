package com.sprint.mission.discodeit.core.notification.repository;

import com.sprint.mission.discodeit.core.notification.entity.Notification;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaNotificationRepository extends JpaRepository<Notification, UUID>,
    CustomNotificationRepository {

}
