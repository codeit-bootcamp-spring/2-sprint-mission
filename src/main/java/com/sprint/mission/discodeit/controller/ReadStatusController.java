package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.CreateReadStatusDTO;
import com.sprint.mission.discodeit.dto.ReadStatusResponseDTO;
import com.sprint.mission.discodeit.dto.UpdateReadStatusDTO;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/read-status")
@RequiredArgsConstructor
public class ReadStatusController {
    private final ReadStatusService readStatusService;

    // POST 특정 채널의 메시지 수신 정보 생성
    @RequestMapping(value = "/channel/{channelId}", method = RequestMethod.POST)
    public ResponseEntity<ReadStatusResponseDTO> createReadStatus(
            @RequestBody CreateReadStatusDTO createReadStatusDTO){
        ReadStatus readStatus = readStatusService.create(createReadStatusDTO);

        return ResponseEntity.ok(new ReadStatusResponseDTO(
                readStatus.getId(),
                readStatus.getUserId(),
                readStatus.getChannelId(),
                readStatus.getLastRead()
        ));
        // 아래 코드는 201상태를 반환해준다.
        // 위 return문 대신 넣어볼만할수도
        // return new ResponseEntity<>(readStatus, HttpStatus.CREATED);
    }

    // PUT 특정 채널의 메시지 수신 정보 업데이트 (얼마나 최근에 읽어왔는지)
    @RequestMapping(value = "/channel/{channelId}", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateReadStatus(
            @PathVariable UUID userId,
            @PathVariable UUID channelId,
            @RequestBody UpdateReadStatusDTO updateReadStatusDTO
    ){
        ReadStatus readStatus = readStatusService.findByChannelIdAndUserId(channelId, userId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // 가장 마지막에 메시지를 수신한 시간을 가지고 있는 readStatus의 Id를 전달
        updateReadStatusDTO.setId(readStatus.getId());
        readStatusService.update(updateReadStatusDTO);
        return ResponseEntity.ok().build();
    }

    // GET 특정 사용자의 메시지를 읽어오기
    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    public ResponseEntity<List<ReadStatusResponseDTO>> getAllReadStatus(@PathVariable UUID userId) {
        List<ReadStatus> readStatuses = readStatusService.findAllByUserId(userId);
        List<ReadStatusResponseDTO> readStatusResponseDTOS = new ArrayList<>();
        // return할 DTO 생성
        readStatuses.forEach(readStatus -> {
            readStatusResponseDTOS.add(new ReadStatusResponseDTO(
                    readStatus.getId(),
                    readStatus.getUserId(),
                    readStatus.getChannelId(),
                    readStatus.getLastRead()
            ));
        });
        return ResponseEntity.ok(readStatusResponseDTOS);
    }
}
