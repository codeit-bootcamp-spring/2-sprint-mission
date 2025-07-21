package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.NotificationApi;
import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import com.sprint.mission.discodeit.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/notifications")
public class NotificationController implements NotificationApi {

    private final NotificationService notificationService;

    private final JwtService jwtService;


    @GetMapping
    public ResponseEntity<List<NotificationDto>> getNotifications( @RequestHeader("Authorization") String authHeader) {
        UUID userId = getUserIdFromToken(authHeader);

        List<NotificationDto> notifications = notificationService.getNotifications(userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(notifications);
    }

    @DeleteMapping(path="/{notificationId}")
    public ResponseEntity<Void> deleteNotification(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable("notificationId") UUID notificationId) {
        UUID userId = getUserIdFromToken(authHeader);

        notificationService.deleteNotification(userId, notificationId);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    private UUID getUserIdFromToken(String authHeader) {
        String token = authHeader.substring(7);
        return jwtService.parse(token).userDto().id();
    }
}
