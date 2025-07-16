package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Notification;
import java.util.List;
import java.util.UUID;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {
  List<Notification> findAllByReceiver_IdOrderByCreatedAtDesc(UUID receiverId);
  void deleteById(@NonNull UUID notificationId);
}
