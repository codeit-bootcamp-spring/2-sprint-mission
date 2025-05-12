package com.sprint.mission.discodeit.core.status.controller;

import com.sprint.mission.discodeit.core.status.controller.dto.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.core.status.controller.dto.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.core.status.usecase.read.ReadStatusService;
import com.sprint.mission.discodeit.core.status.usecase.read.dto.ReadStatusCreateCommand;
import com.sprint.mission.discodeit.core.status.usecase.read.dto.ReadStatusDto;
import com.sprint.mission.discodeit.core.status.usecase.read.dto.ReadStatusUpdateCommand;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Read Status", description = "읽기 상태 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/readStatuses")
public class ReadStatusController {

  private final ReadStatusService readStatusService;

  @PostMapping
  public ResponseEntity<ReadStatusDto> createReadStatus(
      @RequestBody ReadStatusCreateRequest requestBody) {
    ReadStatusCreateCommand command = ReadStatusDtoMapper.toCreateReadStatusCommand(requestBody);
    ReadStatusDto result = readStatusService.create(command);

    return ResponseEntity.status(HttpStatus.CREATED).body(result);
  }

  @PatchMapping("/{readStatusId}")
  public ResponseEntity<ReadStatusDto> updateReadStatus(
      @PathVariable UUID readStatusId,
      @RequestBody ReadStatusUpdateRequest requestBody) {
    ReadStatusUpdateCommand command = ReadStatusDtoMapper.toUpdateReadStatusCommand(readStatusId,
        requestBody);
    ReadStatusDto result = readStatusService.update(command);
    return ResponseEntity.ok(result);
  }

  @GetMapping
  public ResponseEntity<List<ReadStatusDto>> findAllByUserId(
      @RequestParam("userId") UUID userId) {
    List<ReadStatusDto> readStatuses = readStatusService.findAllByUserId(userId);

    return ResponseEntity.ok(readStatuses);
  }

}
