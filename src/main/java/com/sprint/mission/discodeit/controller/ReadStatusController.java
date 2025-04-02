package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.CreateReadStatusRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.jwt.JwtUtil;
import com.sprint.mission.discodeit.jwt.RequiresAuth;
import com.sprint.mission.discodeit.service.ReadStatusService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/readStatuses")
@RequiredArgsConstructor
public class ReadStatusController {

  private final ReadStatusService readStatusService;
  private final JwtUtil jwtUtil;

  @RequiresAuth
  @RequestMapping(value = "", method = RequestMethod.POST)
  public ResponseEntity<?> createReadStatus(
      @RequestBody CreateReadStatusRequest request
  ) {
    readStatusService.createReadStatus(request);

    return ResponseEntity.ok("채널 가입이 완료되었습니다.");
  }

  @RequiresAuth
  @RequestMapping(value = "/{readStatusId}", method = RequestMethod.PUT)
  public ResponseEntity<?> updateReadStatus(
      @PathVariable UUID readStatusId,
      HttpServletRequest httpRequest
  ) {
    UUID userId = (UUID) httpRequest.getAttribute("userId");

    readStatusService.updateReadStatus(readStatusId);

    return ResponseEntity.ok("유저 메세지 수신정보 업데이트 완료되었습니다.");
  }

  @RequiresAuth
  @RequestMapping(value = "/{channelId}", method = RequestMethod.GET)
  public ResponseEntity<?> getReadStatus(
      @PathVariable UUID channelId,
      HttpServletRequest httpRequest
  ) {
    UUID userId = (UUID) httpRequest.getAttribute("userId");

    ReadStatus readStatus = readStatusService.findReadStatusById(userId, channelId);

    return ResponseEntity.ok(readStatus);
  }

  @RequiresAuth
  @RequestMapping(value = "", method = RequestMethod.GET)
  public ResponseEntity<?> findAllByUserId(
      HttpServletRequest httpRequest
  ) {
    UUID userId = (UUID) httpRequest.getAttribute("userId");

    List<ReadStatus> readStatuses = readStatusService.findAll().stream()
        .filter(readStatus -> readStatus.getUserId().equals(userId))
        .toList();

    return ResponseEntity.ok(readStatuses);
  }

}
