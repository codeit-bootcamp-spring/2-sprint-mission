package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.readstatus.CreateReadStatusRequest;
import com.sprint.mission.discodeit.dto.readstatus.UpdateReadStatusRequest;
import com.sprint.mission.discodeit.entity.status.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReadStatusController {

    private final ReadStatusService readStatusService;

    @GetMapping("/api/readStatuses")
    public ResponseEntity<List<ReadStatus>> findAllByUserId(
        @RequestParam UUID userId
    ) {
        List<ReadStatus> response = readStatusService.findAllByUserId(userId);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/readStatuses")
    public ResponseEntity<ReadStatus> create(
        @RequestBody CreateReadStatusRequest request) {

        UUID readStatusId = readStatusService.create(request);
        ReadStatus response = readStatusService.find(readStatusId);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/api/readStatuses/{readStatusId}")
    public ResponseEntity<ReadStatus> updateReadStatus(
        @PathVariable UUID readStatusId,
        @RequestBody UpdateReadStatusRequest request
    ) {
        readStatusService.update(readStatusId, request);
        ReadStatus response = readStatusService.find(readStatusId);

        return ResponseEntity.ok(response);
    }
}
