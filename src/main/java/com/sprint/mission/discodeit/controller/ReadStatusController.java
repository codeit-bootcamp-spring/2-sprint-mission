package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateRequestDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.basic.BasicReadStatusService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/read-status")
@RequiredArgsConstructor
public class ReadStatusController {
    private final BasicReadStatusService readStatusService;

    @PostMapping
    public ResponseEntity<ReadStatus> create(@RequestBody ReadStatusCreateRequestDto dto) {
        return ResponseEntity.ok(readStatusService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadStatus> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(readStatusService.findById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReadStatus>> findAllByUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok(readStatusService.findAllByUserId(userId));
    }

    @PatchMapping
    public ResponseEntity<ReadStatus> update(@RequestBody ReadStatusUpdateRequestDto dto) {
        return ResponseEntity.ok(readStatusService.update(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        readStatusService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
