package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.application.dto.readstatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.application.dto.readstatus.ReadStatusResult;
import com.sprint.mission.discodeit.service.ReadStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/readStatuses")
@RequiredArgsConstructor
public class ReadStatusController {

    private final ReadStatusService readStatusService;

    @PostMapping
    public ResponseEntity<ReadStatusResult> createReadStatus(
            @Valid @RequestBody ReadStatusCreateRequest request) {
        ReadStatusResult readStatusResult = readStatusService.create(request);

        return ResponseEntity.ok(readStatusResult);
    }

    @PutMapping("/{readStatusId}")
    public ResponseEntity<ReadStatusResult> updateReadStatus(@PathVariable UUID readStatusId) {
        ReadStatusResult readStatusResult = readStatusService.updateLastReadTime(readStatusId);

        return ResponseEntity.ok(readStatusResult);
    }

    @GetMapping
    public ResponseEntity<List<ReadStatusResult>> getUserReadStatus(@RequestParam UUID userId) {
        List<ReadStatusResult> readStatusResults = readStatusService.getAllByUserId(userId);

        return ResponseEntity.ok(readStatusResults);
    }

    @DeleteMapping("/{readStatusId}")
    public ResponseEntity<Void> delete(@PathVariable UUID readStatusId) {
        readStatusService.delete(readStatusId);

        return ResponseEntity.noContent().build();
    }
}