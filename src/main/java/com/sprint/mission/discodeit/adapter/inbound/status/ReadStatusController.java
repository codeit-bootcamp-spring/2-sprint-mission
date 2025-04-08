package com.sprint.mission.discodeit.adapter.inbound.status;

import static com.sprint.mission.discodeit.adapter.inbound.status.ReadStatusDtoMapper.toCreateReadStatusCommand;
import static com.sprint.mission.discodeit.adapter.inbound.status.ReadStatusDtoMapper.toUpdateReadStatusCommand;

import com.sprint.mission.discodeit.adapter.inbound.status.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.adapter.inbound.status.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.adapter.inbound.status.response.ReadStatusCreateResponse;
import com.sprint.mission.discodeit.adapter.inbound.status.response.ReadStatusFindResponse;
import com.sprint.mission.discodeit.adapter.inbound.status.response.ReadStatusUpdateResponse;
import com.sprint.mission.discodeit.core.status.entity.ReadStatus;
import com.sprint.mission.discodeit.core.status.usecase.read.ReadStatusService;
import com.sprint.mission.discodeit.core.status.usecase.read.dto.CreateReadStatusCommand;
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
  public ResponseEntity<ReadStatusCreateResponse> createReadStatus(
      @RequestBody ReadStatusCreateRequest requestBody) {
    CreateReadStatusCommand command = toCreateReadStatusCommand(requestBody);
    ReadStatus status = readStatusService.create(command);

    return ResponseEntity.ok(ReadStatusCreateResponse.create(status));
  }

  @PatchMapping
  public ResponseEntity<ReadStatusUpdateResponse> updateReadStatus(
      @RequestParam UUID readStatusId,
      @RequestBody ReadStatusUpdateRequest requestBody) {
    UpdateReadStatusCommand command = toUpdateReadStatusCommand(readStatusId, requestBody);
    ReadStatus status = readStatusService.updateReadStatus(command);
    return ResponseEntity.ok(ReadStatusUpdateResponse.create(status));
  }

  @GetMapping
  public ResponseEntity<List<ReadStatusFindResponse>> findAllByUserId(
      @RequestParam("userId") UUID userId) {
    List<ReadStatus> readStatuses = readStatusService.findAllByUserId(userId);
    List<ReadStatusFindResponse> list = new ArrayList<>();

    for (ReadStatus readStatus : readStatuses) {
      list.add(ReadStatusFindResponse.create(readStatus));
    }

    return ResponseEntity.ok(list);
  }

}
