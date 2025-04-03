package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.ReadStatusApi;
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
public class ReadStatusController implements ReadStatusApi {

  private final BasicReadStatusService readStatusService;

  @Override
  @PostMapping
  public ResponseEntity<ReadStatus> create(
      @RequestBody ReadStatusCreateRequest readStatusCreateRequest) {

    ReadStatus createdReadStatus = readStatusService.create(readStatusCreateRequest);

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(createdReadStatus);
  }

  @Override
  @PutMapping("/{readStatusId}")
  public ResponseEntity<ReadStatus> update(
      @PathVariable("readStatusId") UUID readStatusId,
      @RequestBody ReadStatusUpdateRequest readStatusUpdateRequest) {

    ReadStatus updatedReadStatus = readStatusService.update(readStatusId, readStatusUpdateRequest);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(updatedReadStatus);
  }

  @Override
  @GetMapping
  public ResponseEntity<List<ReadStatus>> findAllByUserId(
      @RequestParam("userId") UUID userId) {

    List<ReadStatus> readStatuses = readStatusService.findAllByUserId(userId);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(readStatuses);
  }
}
