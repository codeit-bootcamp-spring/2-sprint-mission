package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.Api.ReadStatusApi;
import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;

@RequiredArgsConstructor
@Controller
@ResponseBody
//@RequestMapping("/api/readStatus")
public class ReadStatusController implements ReadStatusApi {

  private final ReadStatusService readStatusService;

  @Override
  public ResponseEntity<ReadStatus> create1(ReadStatusCreateRequest readStatusCreateRequest) {
    ReadStatus createdReadStatus =  readStatusService.create(readStatusCreateRequest);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(createdReadStatus);
  }

  @Override
  public ResponseEntity<Object> findAllByUserId(Object userId) {
    UUID uuid = UUID.fromString(userId.toString());
    List<ReadStatus> readStatuses =  readStatusService.findAllByUserId(uuid);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(readStatuses);
  }

  @Override
  public ResponseEntity<ReadStatus> update1(Object readStatusId,
      ReadStatusUpdateRequest readStatusUpdateRequest) {
    UUID uuid = UUID.fromString(readStatusId.toString());
    ReadStatus updatedReadStatus = readStatusService.update(uuid, readStatusUpdateRequest);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(updatedReadStatus);
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
