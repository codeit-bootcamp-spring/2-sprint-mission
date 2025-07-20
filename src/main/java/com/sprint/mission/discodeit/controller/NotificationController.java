package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.notification.NotificationDto;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.service.NotificationService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping()
    public ResponseEntity<List<NotificationDto>> findAllByReceiverId(
        @AuthenticationPrincipal DiscodeitUserDetails principal
    ) {
        List<NotificationDto> notificationDtos = notificationService.readNotifications(principal);
        return ResponseEntity.ok(notificationDtos);
    }

    @DeleteMapping("/{notificationId}")
    @PreAuthorize("principal.userId == @basicNotificationService.readNotification(#notificationId).receiverId()")
    public ResponseEntity<Void> deleteByNotificationId(
        @PathVariable UUID notificationId
    ) {
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.noContent().build();
    }

}
