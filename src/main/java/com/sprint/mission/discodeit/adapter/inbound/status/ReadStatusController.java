package com.sprint.mission.discodeit.adapter.inbound.status;

import com.sprint.mission.discodeit.adapter.inbound.status.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.adapter.inbound.status.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.adapter.inbound.status.response.ReadStatusResponse;
import com.sprint.mission.discodeit.core.status.usecase.read.ReadStatusService;
import com.sprint.mission.discodeit.core.status.usecase.read.dto.CreateReadStatusCommand;
import com.sprint.mission.discodeit.core.status.usecase.read.dto.ReadStatusResult;
import com.sprint.mission.discodeit.core.status.usecase.read.dto.UpdateReadStatusCommand;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
  public ResponseEntity<ReadStatusResponse> createReadStatus(
      @RequestBody ReadStatusCreateRequest requestBody) {
    CreateReadStatusCommand command = ReadStatusDtoMapper.toCreateReadStatusCommand(requestBody);
    ReadStatusResult result = readStatusService.create(command);

    return ResponseEntity.ok(ReadStatusDtoMapper.toCreateResponse(result));
  }

  @PatchMapping
  public ResponseEntity<ReadStatusResponse> updateReadStatus(
      @RequestParam UUID readStatusId,
      @RequestBody ReadStatusUpdateRequest requestBody) {
    UpdateReadStatusCommand command = ReadStatusDtoMapper.toUpdateReadStatusCommand(readStatusId,
        requestBody);
    ReadStatusResult result = readStatusService.updateReadStatus(command);
    return ResponseEntity.ok(ReadStatusDtoMapper.toCreateResponse(result));
  }

  @GetMapping
  public ResponseEntity<List<ReadStatusResponse>> findAllByUserId(
      @RequestParam("userId") UUID userId) {
    List<ReadStatusResult> readStatuses = readStatusService.findAllByUserId(userId);
    List<ReadStatusResponse> list = new ArrayList<>();

    for (ReadStatusResult readStatus : readStatuses) {
      list.add(ReadStatusDtoMapper.toCreateResponse(readStatus));
    }

    return ResponseEntity.ok(list);
  }

}
