package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusResponse;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusUpdateRequest;
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

  @GetMapping
  public ResponseEntity<List<ReadStatusResponse>> findAllByUser(@RequestParam UUID userId) {
    List<ReadStatusResponse> statuses = readStatusService.findAllByUserId(userId);
    return ResponseEntity.ok(statuses);
  }

  @PostMapping
  public ResponseEntity<ReadStatusResponse> create(@RequestBody ReadStatusCreateRequest request) {
    ReadStatusResponse readStatus = readStatusService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(readStatus);
  }

  @PatchMapping("/{readStatusId}")
  public ResponseEntity<ReadStatusResponse> update(@PathVariable UUID readStatusId,
      @RequestBody ReadStatusUpdateRequest readStatusUpdateRequest) {
    ReadStatusResponse updatedStatus = readStatusService.update(readStatusId,
        readStatusUpdateRequest.newLastReadAt());
    return ResponseEntity.ok(updatedStatus);
  }
}
