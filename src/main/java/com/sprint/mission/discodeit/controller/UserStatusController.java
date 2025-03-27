package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/userstatus")
public class UserStatusController {
    private final UserStatusService userStatusService;

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateUserStatus(
            @RequestParam UUID userId,
            @RequestParam boolean isOnline) {

        UserStatusUpdateRequest updateRequest = new UserStatusUpdateRequest(isOnline ? Instant.now() : null);
        userStatusService.updateByUserId(userId, updateRequest);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}