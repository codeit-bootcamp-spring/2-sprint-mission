package com.sprint.mission.discodeit.adapter.inbound.message;

import com.sprint.mission.discodeit.adapter.inbound.message.dto.ReadStatusCreateRequestDTO;
import com.sprint.mission.discodeit.adapter.inbound.message.dto.ReadStatusUpdateRequestDTO;
import com.sprint.mission.discodeit.core.message.entity.ReadStatus;
import com.sprint.mission.discodeit.core.message.usecase.ReadStatusService;
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
      @PathVariable UUID channelId, @RequestBody ReadStatusCreateRequestDTO requestDTO) {
    ReadStatus status = readStatusService.create(userId, channelId, requestDTO);

    return ResponseEntity.ok(status);
  }

  @PutMapping("/update")
  public ResponseEntity<ReadStatus> update(@PathVariable UUID channelId,
      @RequestBody ReadStatusUpdateRequestDTO requestDTO) {
    ReadStatus status = readStatusService.update(channelId, requestDTO);
    return ResponseEntity.ok(status);
  }

  @GetMapping("/find")
  public ResponseEntity<ReadStatus> find(@PathVariable UUID userId) {
    ReadStatus readStatus = readStatusService.findByUserId(userId);
    return ResponseEntity.ok(readStatus);
  }

}
