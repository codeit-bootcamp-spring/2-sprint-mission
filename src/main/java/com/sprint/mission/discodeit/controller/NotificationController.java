package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.service.NotificationService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

  private final NotificationService notificationService;

  @GetMapping
  public ResponseEntity<List<NotificationDto>> findAllByReceiverId(
      @AuthenticationPrincipal DiscodeitUserDetails principal) {
    UUID receiverId = principal.getUserDto().id();

    List<NotificationDto> notifications = notificationService.findAllByReceiverId(receiverId);
    return ResponseEntity.ok(notifications);
  }

  @DeleteMapping("/{notificationId}")
  public ResponseEntity<Void> delete(
      @AuthenticationPrincipal DiscodeitUserDetails principal,
      @PathVariable UUID notificationId) {
    UUID receiverId = principal.getUserDto().id();

    notificationService.delete(notificationId, receiverId);
    return ResponseEntity.noContent().build();
  }
}
