package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.jwt.JwtUtil;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/readStatus")
@RequiredArgsConstructor
public class ReadStatusController {

    private final ReadStatusService readStatusService;
    private final JwtUtil jwtUtil;

    @RequestMapping(value = "/channel/{channelId}", method = RequestMethod.POST)
    public ResponseEntity<?> createReadStatus(
            @PathVariable UUID channelId,
            @RequestHeader("Authorization") String authHeader
    ) {

        String token = authHeader.replace("Bearer ", "");
        UUID userId = UUID.fromString(jwtUtil.extractUserId(token));

        readStatusService.createReadStatus(userId, channelId);

        return ResponseEntity.ok("채널 가입이 완료되었습니다.");
    }

    @RequestMapping(value = "/channel/{channelId}", method = RequestMethod.POST)
    public ResponseEntity<?> updateReadStatus(
            @PathVariable UUID channelId,
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = authHeader.replace("Bearer ", "");
        UUID userId = UUID.fromString(jwtUtil.extractUserId(token));

        readStatusService.updateReadStatus(userId, channelId);

        return ResponseEntity.ok("유저 메세지 수신정보 업데이트 완료되었습니다.");
    }

    @RequestMapping(value = "/channel/{channelId}", method = RequestMethod.GET)
    public ResponseEntity<?> getReadStatus(
            @PathVariable UUID channelId,
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = authHeader.replace("Bearer ", "");
        UUID userId = UUID.fromString(jwtUtil.extractUserId(token));

        ReadStatus readStatus = readStatusService.findReadStatusById(userId, channelId);

        return ResponseEntity.ok(readStatus);
    }

}
