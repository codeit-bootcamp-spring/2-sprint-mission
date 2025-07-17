package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.controller.notification.NotificationDto;
import com.sprint.mission.discodeit.service.basic.BasicNotificationService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

  private final BasicNotificationService notificationService;

  @GetMapping
  public ResponseEntity<List<NotificationDto>> getNotification() {
    return ResponseEntity.ok(notificationService.findAll());
  }

  @DeleteMapping("/{notificationId}")
  public ResponseEntity<Void> deleteNotification(
      @PathVariable("notificationId") UUID notificationId) {
    notificationService.delete(notificationId);
    return ResponseEntity.noContent().build();
  }
}
