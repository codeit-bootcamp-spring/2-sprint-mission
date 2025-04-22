package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.ReadStatusDto;
import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
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
public class ReadStatusController {
    private final ReadStatusService readStatusService;
    private final ReadStatusMapper readStatusMapper;

    @PostMapping
    public ResponseEntity<ReadStatus> createReadStatus(@RequestBody ReadStatusCreateRequest readStatusCreateRequest) {
        ReadStatus createReadStatus = readStatusService.create(readStatusCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createReadStatus);
    }

    @PatchMapping("/{readStatusId}")
    public ResponseEntity<ReadStatus> updateReadStatus(@PathVariable UUID readStatusId, @RequestBody ReadStatusUpdateRequest readStatusUpdateRequest) {
        ReadStatus updateReadStatus = readStatusService.update(readStatusId, readStatusUpdateRequest);
        return ResponseEntity.ok(updateReadStatus);
    }

    @GetMapping
    public ResponseEntity<List<ReadStatusDto>> getReadStatus(@RequestParam UUID userId) {
        List<ReadStatusDto> dtos = readStatusService.findAllByUserId(userId).stream()
                .map(readStatusMapper::toDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

}