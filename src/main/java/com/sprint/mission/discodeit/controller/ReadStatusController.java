package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.ReadStatusApi;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/readStatuses")
@RequiredArgsConstructor
public class ReadStatusController implements ReadStatusApi {

    private final ReadStatusService readStatusService;

    @Override
    @PostMapping
    public ResponseEntity<ReadStatusDto> create(@RequestBody ReadStatusCreateRequest request) {
        ReadStatusDto readStatus = readStatusService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(readStatus);
    }

    @Override
    @GetMapping
    public ResponseEntity<List<ReadStatusDto>> findAllByUserId(
            @RequestParam UUID userId) {
        List<ReadStatusDto> statuses = readStatusService.findAllByUserId(userId);

        return ResponseEntity.status(HttpStatus.OK).body(statuses);
    }

    @Override
    @PatchMapping("/{readStatusId}")
    public ResponseEntity<ReadStatusDto> updateReadStatusById(
            @PathVariable UUID readStatusId,
            @RequestBody ReadStatusUpdateRequest request) {
        ReadStatusDto updatedReadStatus = readStatusService.update(readStatusId, request);
        return ResponseEntity.status(HttpStatus.OK).body(updatedReadStatus);
    }
}
