package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.readStatus.CreateReadStatusRequest;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.UpdateReadStatusRequest;
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
  public ResponseEntity<ReadStatusDto> createReadStatus(
      @RequestBody CreateReadStatusRequest request
  ) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(readStatusService.createReadStatus(request));
  }

  @RequestMapping(value = "", method = RequestMethod.GET)
  public ResponseEntity<List<ReadStatusDto>> findAllByUserId(
      @RequestParam("userId") UUID userId
  ) {
    return ResponseEntity.ok(readStatusService.findAllByUserId(userId));
  }

  @RequestMapping(value = "/{readStatusId}", method = RequestMethod.PATCH)
  public ResponseEntity<ReadStatusDto> updateReadStatus(
      @PathVariable("readStatusId") UUID readStatusId,
      @RequestBody UpdateReadStatusRequest request
  ) {
    return ResponseEntity.ok(readStatusService.updateReadStatus(readStatusId, request));
  }

  @RequestMapping(value = "/{channelId}", method = RequestMethod.GET)
  public ResponseEntity<ReadStatusDto> getReadStatus(
      @RequestParam("userId") UUID userId,
      @PathVariable("channelId") UUID channelId
  ) {

    return ResponseEntity.ok(
        readStatusService.findReadStatusByUserIdAndChannelId(userId, channelId));
  }


}
