package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.dto.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
@ResponseBody
@RequestMapping("/api/readStatus")
public class ReadStatusController implements ReadStatusApi {

  private final ReadStatusService readStatusService;

  @Override
  public ResponseEntity<ReadStatus> create1(ReadStatusCreateRequest readStatusCreateRequest) {
    readStatusService.create(readStatusCreateRequest);
    return ReadStatusApi.super.create1(readStatusCreateRequest);
  }

  @Override
  public ResponseEntity<List<ReadStatus>> findAllByUserId(UUID userId) {
    return ReadStatusApi.super.findAllByUserId(userId);
  }

  @Override
  public ResponseEntity<ReadStatus> update1(UUID readStatusId,
      ReadStatusUpdateRequest readStatusUpdateRequest) {
    return ReadStatusApi.super.update1(readStatusId, readStatusUpdateRequest);
  }

  /*
  @RequestMapping(path = "create")
  public ResponseEntity<ReadStatus> create(@RequestBody ReadStatusCreateRequest request) {
    ReadStatus createdReadStatus = readStatusService.create(request);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(createdReadStatus);
  }

  @RequestMapping(path = "update")
  public ResponseEntity<ReadStatus> update(@RequestParam("readStatusId") UUID readStatusId,
      @RequestBody ReadStatusUpdateRequest request) {
    ReadStatus updatedReadStatus = readStatusService.update(readStatusId, request);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(updatedReadStatus);
  }

  @RequestMapping(path = "findAllByUserId")
  public ResponseEntity<List<ReadStatus>> findAllByUserId(@RequestParam("userId") UUID userId) {
    List<ReadStatus> readStatuses = readStatusService.findAllByUserId(userId);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(readStatuses);
  }*/
}
