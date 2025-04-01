package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.application.dto.readstatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.application.dto.readstatus.ReadStatusResult;
import com.sprint.mission.discodeit.service.ReadStatusService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/readStatuses")
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

  @GetMapping("/{userId}")
  public ResponseEntity<List<ReadStatusResult>> getUserReadStatus(@PathVariable UUID userId) {
    List<ReadStatusResult> readStatusResults = readStatusService.getAllByUserId(userId);
    return ResponseEntity.ok(readStatusResults);
  }
}