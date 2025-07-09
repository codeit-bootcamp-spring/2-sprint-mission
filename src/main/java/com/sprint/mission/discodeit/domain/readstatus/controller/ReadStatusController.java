package com.sprint.mission.discodeit.domain.readstatus.controller;

import com.sprint.mission.discodeit.domain.readstatus.service.ReadStatusService;
import com.sprint.mission.discodeit.domain.readstatus.dto.ReadStatusResult;
import com.sprint.mission.discodeit.domain.readstatus.dto.request.ReadStatusCreateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/readStatuses")
@RequiredArgsConstructor
public class ReadStatusController {

    private final ReadStatusService readStatusService;

    @PostMapping
    public ResponseEntity<ReadStatusResult> create(@Valid @RequestBody ReadStatusCreateRequest request) {
        ReadStatusResult readStatusResult = readStatusService.create(request);

        return ResponseEntity.ok(readStatusResult);
    }

    @PatchMapping("/{readStatusId}")
    public ResponseEntity<ReadStatusResult> update(@PathVariable UUID readStatusId) {
        ReadStatusResult readStatusResult = readStatusService.updateLastReadTime(readStatusId, Instant.now());

        return ResponseEntity.ok(readStatusResult);
    }

    @GetMapping
    public ResponseEntity<List<ReadStatusResult>> getAllByUserId(@RequestParam UUID userId) {
        List<ReadStatusResult> readStatusResults = readStatusService.getAllByUserId(userId);

        return ResponseEntity.ok(readStatusResults);
    }

    @DeleteMapping("/{readStatusId}")
    public ResponseEntity<Void> delete(@PathVariable UUID readStatusId) {
        readStatusService.delete(readStatusId);

        return ResponseEntity.noContent().build();
    }

}