package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.NotificationApi;
import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/notifications")
public class NotificationController implements NotificationApi {

    private final NotificationService notificationService;

    @Override
    @GetMapping
    public ResponseEntity<List<NotificationDto>> findNotifications(Authentication authentication) {
            UUID receiverId = (UUID.fromString(authentication.getName()));

        return ResponseEntity.ok(notificationService.getNotifications(receiverId));
    }

    @Override
    public ResponseEntity<Void> deleteNotifications(UUID notificationId, Authentication authentication) {
        UUID receiverId = (UUID.fromString(authentication.getName()));
        notificationService.deleteNotification(notificationId,receiverId);
        return ResponseEntity.noContent().build();
    }
}
