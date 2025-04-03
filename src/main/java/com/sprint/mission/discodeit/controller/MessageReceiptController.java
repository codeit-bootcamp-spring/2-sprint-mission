package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.dto.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.controller.dto.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity._ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/readStatus")
public class MessageReceiptController {

  private final ReadStatusService readStatusService;

  public MessageReceiptController(ReadStatusService readStatusService) {
    this.readStatusService = readStatusService;
  }

  @PostMapping
  public ResponseEntity<_ReadStatus> createReceipt(@RequestBody ReadStatusCreateRequest request) {
    _ReadStatus receipt = readStatusService.create(request);
    return ResponseEntity.ok(receipt);
  }

  @PatchMapping("/{readStatusId}")
  public ResponseEntity<_ReadStatus> updateReceipt(@PathVariable UUID receiptId,
      @RequestParam ReadStatusUpdateRequest request) {
    return ResponseEntity.ok(readStatusService.update(receiptId, request));
  }

  @GetMapping("/{readStatusId}")
  public ResponseEntity<_ReadStatus> getReadStatus(@PathVariable UUID readStatusId) {
    return ResponseEntity.ok(readStatusService.find(readStatusId));
  }
}
