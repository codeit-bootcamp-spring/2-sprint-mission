package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/channels/{channelId}/read-status")
public class ReadStatusController {
    private final ReadStatusService readStatusService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<ReadStatus> createReadStatus(
            @PathVariable UUID channelId,
            @RequestBody ReadStatusCreateRequest request
    ) {
        ReadStatus createdReadStatus = readStatusService.create(request);
        return ResponseEntity.ok(createdReadStatus);
    }

    @RequestMapping(value = "/update/{readStatusId}", method = RequestMethod.PUT)
    public ResponseEntity<ReadStatus> updateReadStatus(
            @PathVariable UUID channelId,
            @PathVariable UUID readStatusId,
            @RequestBody ReadStatusUpdateRequest request
    ) {
        ReadStatus updatedReadStatus = readStatusService.update(readStatusId, request);
        return ResponseEntity.ok(updatedReadStatus);
    }

    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    public ResponseEntity<List<ReadStatus>> getUserReadStatuses(@PathVariable UUID userId) {
        List<ReadStatus> userReadStatuses = readStatusService.findAllByUserId(userId);
        return ResponseEntity.ok(userReadStatuses);
    }
}