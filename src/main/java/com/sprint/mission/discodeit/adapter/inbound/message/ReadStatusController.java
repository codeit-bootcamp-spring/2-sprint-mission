package com.sprint.mission.discodeit.adapter.inbound.message;

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
@RequestMapping("/api/{userId}/{channelId}/readstatus")
public class ReadStatusController {

  private final ReadStatusService readStatusService;

  @PostMapping("/create")
  public ResponseEntity<ReadStatus> createReadStatus(@PathVariable UUID userId,
      @PathVariable UUID channelId, @RequestBody CreateReadStatusCommand requestDTO) {
    ReadStatus status = readStatusService.create(userId, channelId, requestDTO);

    return ResponseEntity.ok(status);
  }

  @PutMapping("/update")
  public ResponseEntity<ReadStatus> update(@PathVariable UUID channelId,
      @RequestBody UpdateReadStatusCommand requestDTO) {
    ReadStatus status = readStatusService.updateReadStatus(channelId, requestDTO);
    return ResponseEntity.ok(status);
  }

  @GetMapping("/find")
  public ResponseEntity<ReadStatus> find(@PathVariable UUID userId) {
    ReadStatus readStatus = readStatusService.findReadStatusByUserId(userId);
    return ResponseEntity.ok(readStatus);
  }

}
