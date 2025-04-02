package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.basic.BasicReadStatusService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/readStatus")
@Tag(name = "ReadStatus", description = "Message 읽음 상태 API")
public class ReadStatusController {

  private final BasicReadStatusService readStatusService;

  @PostMapping
  public ResponseEntity<ReadStatus> createReadStatus(
      @RequestBody ReadStatusCreateRequest readStatusCreateRequest) {

    ReadStatus createdReadStatus = readStatusService.create(readStatusCreateRequest);

    return new ResponseEntity<>(createdReadStatus, HttpStatus.CREATED);
  }

  @PutMapping("/{readStatusId}")
  public ResponseEntity<ReadStatus> updateReadStatus(
      @PathVariable UUID readStatusId,
      @RequestBody ReadStatusUpdateRequest readStatusUpdateRequest) {

    ReadStatus updatedReadStatus = readStatusService.update(readStatusId, readStatusUpdateRequest);

    return new ResponseEntity<>(updatedReadStatus, HttpStatus.OK);
  }

  @GetMapping("/users/{userId}")
  public ResponseEntity<List<ReadStatus>> getReadStatusByUser(
      @PathVariable UUID userId) {

    List<ReadStatus> readStatuses = readStatusService.findAllByUserId(userId);

    return new ResponseEntity<>(readStatuses, HttpStatus.OK);
  }
}
