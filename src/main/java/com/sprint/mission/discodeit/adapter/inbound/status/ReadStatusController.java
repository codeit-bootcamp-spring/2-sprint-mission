package com.sprint.mission.discodeit.adapter.inbound.status;

import static com.sprint.mission.discodeit.adapter.inbound.status.ReadStatusDtoMapper.toCreateReadStatusCommand;
import static com.sprint.mission.discodeit.adapter.inbound.status.ReadStatusDtoMapper.toUpdateReadStatusCommand;

import com.sprint.mission.discodeit.adapter.inbound.status.dto.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.adapter.inbound.status.dto.ReadStatusCreateResponse;
import com.sprint.mission.discodeit.adapter.inbound.status.dto.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.adapter.inbound.status.dto.ReadStatusUpdateResponse;
import com.sprint.mission.discodeit.core.status.usecase.read.dto.CreateReadStatusCommand;
import com.sprint.mission.discodeit.core.status.usecase.read.dto.UpdateReadStatusCommand;
import com.sprint.mission.discodeit.core.status.entity.ReadStatus;
import com.sprint.mission.discodeit.core.status.usecase.read.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/readStatus")
public class ReadStatusController {

  private final ReadStatusService readStatusService;

  @PostMapping("/create")
  public ResponseEntity<ReadStatusCreateResponse> createReadStatus(
      @RequestBody ReadStatusCreateRequest requestBody) {
    CreateReadStatusCommand command = toCreateReadStatusCommand(requestBody);
    ReadStatus status = readStatusService.create(command);

    return ResponseEntity.ok(new ReadStatusCreateResponse(true, status.getReadStatusId()));
  }

  @PutMapping
  public ResponseEntity<ReadStatusUpdateResponse> update(
      @RequestParam("readStatusId") UUID readStatusId,
      @RequestBody
      ReadStatusUpdateRequest requestBody) {
    UpdateReadStatusCommand command = toUpdateReadStatusCommand(readStatusId,
        requestBody);
    readStatusService.updateReadStatus(command);
    return ResponseEntity.ok(new ReadStatusUpdateResponse(true));
  }

  @GetMapping
  public ResponseEntity<ReadStatus> findByUserId(@RequestParam UUID userId) {
    ReadStatus readStatus = readStatusService.findReadStatusByUserId(userId);
    return ResponseEntity.ok(readStatus);
  }

}
