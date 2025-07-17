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
  public ResponseEntity<List<NotificationDto>> findAll(
      @AuthenticationPrincipal DiscodeitUserDetails userDetails) {
    log.debug("사용자 전체 알림 조회 요청");
    UUID receiverId = userDetails.getUserDto().id();
    List<NotificationDto> res = notificationService.findAll(receiverId);

    log.debug("사용자 전체 알림 조회 응답 - 결과 크기 : {}", res.size());
    return ResponseEntity.ok(res);
  }

  @DeleteMapping("/{notificationId}")
  public ResponseEntity<Void> delete(
      @AuthenticationPrincipal DiscodeitUserDetails userDetails,
      @PathVariable UUID notificationId) {
    log.debug("알림 삭제 요청");
    UUID receiverId = userDetails.getUserDto().id();

    notificationService.delete(receiverId, notificationId);
    log.debug("알림 삭제 응답");
    return ResponseEntity.ok().build();
  }
}
