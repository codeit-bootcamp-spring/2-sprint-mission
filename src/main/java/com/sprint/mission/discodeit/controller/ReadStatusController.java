package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.util.LogMapUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/readStatuses")
@Tag(name = "ReadStatus", description = "Message 읽음 상태 API")
public class ReadStatusController {

  private final ReadStatusService readStatusService;
  private static final Logger log = LoggerFactory.getLogger(ReadStatusController.class);

  @PostMapping
  public ResponseEntity<ReadStatus> create(@RequestBody ReadStatusCreateRequest request) {
    ReadStatus readStatus = readStatusService.create(request);
    log.info("{}", LogMapUtil.of("action", "create")
        .add("readStatus", readStatus));

    return ResponseEntity.status(HttpStatus.CREATED).body(readStatus);
  }

  @PatchMapping({"/{readStatusId}"})
  public ResponseEntity<ReadStatus> update(@PathVariable UUID readStatusId,
      @RequestBody ReadStatusUpdateRequest request) {
    ReadStatus updated = readStatusService.update(readStatusId, request);
    log.info("{}", LogMapUtil.of("action", "update")
        .add("updated", updated));

    return ResponseEntity.ok(updated);
  }

  @GetMapping
  public ResponseEntity<List<ReadStatus>> readAllByUserKey(@RequestParam UUID userId) {
    List<ReadStatus> list = readStatusService.findAllByUserKey(userId);
    log.info("{}", LogMapUtil.of("action", "readAllByUser")
        .add("list", list));

    return ResponseEntity.ok(list);
  }
}
