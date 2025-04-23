package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.readstatus.CreateReadStatusRequest;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readstatus.UpdateReadStatusRequest;
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
    public ResponseEntity<List<ReadStatusDto>> findAllByUserId(
        @RequestParam UUID userId
    ) {
        List<ReadStatusDto> response = readStatusService.findAllByUserId(userId);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/readStatuses")
    public ResponseEntity<ReadStatusDto> create(
        @RequestBody CreateReadStatusRequest request) {

        ReadStatusDto response = readStatusService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/api/readStatuses/{readStatusId}")
    public ResponseEntity<ReadStatusDto> updateReadStatus(
        @PathVariable UUID readStatusId,
        @RequestBody UpdateReadStatusRequest request
    ) {
        ReadStatusDto response = readStatusService.update(readStatusId, request);

        return ResponseEntity.ok(response);
    }
}
