package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.readStatus.CreateReadStatusRequest;
import com.sprint.mission.discodeit.dto.readStatus.UpdateReadStatusRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/readStatuses")
public class ReadStatusController {

  private final ReadStatusService readStatusService;

  @RequestMapping(value = "", method = RequestMethod.POST)
  public ResponseEntity<ReadStatus> createReadStatus(
      @RequestBody CreateReadStatusRequest request
  ) {
    ReadStatus readStatus = readStatusService.createReadStatus(request);

    return ResponseEntity.status(HttpStatus.CREATED).body(readStatus);
  }

  @RequestMapping(value = "/{readStatusId}", method = RequestMethod.PATCH)
  public ResponseEntity<ReadStatus> updateReadStatus(
      @PathVariable("readStatusId") UUID readStatusId,
      @RequestBody UpdateReadStatusRequest request
  ) {
    ReadStatus readStatus = readStatusService.updateReadStatus(readStatusId, request);

    return ResponseEntity.ok(readStatus);
  }

  @RequestMapping(value = "/{channelId}", method = RequestMethod.GET)
  public ResponseEntity<ReadStatus> getReadStatus(
      @RequestParam("userId") UUID userId,
      @PathVariable("channelId") UUID channelId
  ) {
    ReadStatus readStatus = readStatusService.findReadStatusByUserIdAndChannelId(userId, channelId);

    return ResponseEntity.ok(readStatus);
  }

  @RequestMapping(value = "", method = RequestMethod.GET)
  public ResponseEntity<List<ReadStatus>> findAllByUserId(
      @RequestParam("userId") UUID userId
  ) {

    List<ReadStatus> readStatuses = readStatusService.findAll().stream()
        .filter(readStatus -> readStatus.getUser().getId().equals(userId))
        .toList();

    return ResponseEntity.ok(readStatuses);
  }

}
