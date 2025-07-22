package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.service.NotificationService;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/notifications")
public class NotificationController {

  private final NotificationService notificationService;

  @GetMapping
  public ResponseEntity<List<NotificationDto>> getNotifications(
      @AuthenticationPrincipal DiscodeitUserDetails userDetails
  ) {
    UUID recevierId = userDetails.getUserDto().id();
    log.info("알림 목록 조회 요청: id={}", recevierId);
    List<NotificationDto> notifications =
        notificationService.findByRecevierId(recevierId);
    log.debug("알림 목록 조회 응답: totalElements = {}", notifications.size());
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(notifications);
  }

  @DeleteMapping("/api/notifications/{notificationId}")
  public ResponseEntity<Void> deleteNotification(
      @AuthenticationPrincipal DiscodeitUserDetails userDetails,
      @PathVariable UUID notificationId) throws AccessDeniedException {
    UUID receiverId = userDetails.getUserDto().id();
    notificationService.delete(notificationId, receiverId);
    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .build();
  }
}
