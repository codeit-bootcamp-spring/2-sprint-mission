package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Notification;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {

  List<Notification> findAllByReceiverId(UUID userId);

  @Modifying
  @Query("DELETE FROM Notification n WHERE n.id = :notificationId AND n.receiverId = :receiverId")
  void deleteByIdAndReceiverId(UUID notificationId, UUID receiverId);
}
