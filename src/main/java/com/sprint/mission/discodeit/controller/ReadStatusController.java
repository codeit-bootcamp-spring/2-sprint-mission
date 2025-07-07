package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.swagger.ReadStatusApi;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusRequest;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/readStatuses")
@RequiredArgsConstructor
public class ReadStatusController implements ReadStatusApi {

  private final ReadStatusService readStatusService;

  @PostMapping("")
  @Override
  @PreAuthorize("principal.userId == readStatusRequest.userId()")
  public ResponseEntity<ReadStatusDto> save(
      @RequestBody ReadStatusRequest readStatusRequest
  ) {
    ReadStatusDto readStatusDto = readStatusService.save(readStatusRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(readStatusDto);
  }

  @PatchMapping("/{readStatusId}")
  @Override
  @PostAuthorize("principal.userId == returnObject.body.userId()")
  public ResponseEntity<ReadStatusDto> update(
      @PathVariable("readStatusId") UUID readStatusId,
      @RequestBody ReadStatusUpdateRequest readStatusUpdateRequest) {
    ReadStatusDto readStatusDto = readStatusService.update(readStatusId, readStatusUpdateRequest);
    return ResponseEntity.status(HttpStatus.OK).body(readStatusDto);
  }

  @GetMapping("")
  @Override
  public ResponseEntity<List<ReadStatusDto>> findByUserId(
      @RequestParam("userId") UUID userId
  ) {
    List<ReadStatusDto> readStatusDtoList = readStatusService.findAllByUserId(
        userId);
    return ResponseEntity.status(HttpStatus.OK).body(readStatusDtoList);
  }
}
