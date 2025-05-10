package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.ReadStatusApi;
import com.sprint.mission.discodeit.dto.data.ReadStatusDto;
import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/readStatuses")
public class ReadStatusController implements ReadStatusApi {

    private final ReadStatusService readStatusService;

    @PostMapping
    public ResponseEntity<ReadStatusDto> create(@RequestBody ReadStatusCreateRequest request) {
        // log
        log.info("ReadStaus 요청 들어옴, 생성 시간: {}", request.lastReadAt());
        ReadStatusDto createdReadStatus = readStatusService.create(request);
        log.info("ReadStatus 생성 완료");
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(createdReadStatus);
    }

    @PatchMapping(path = "{readStatusId}")
    public ResponseEntity<ReadStatusDto> update(@PathVariable("readStatusId") UUID readStatusId,
        @RequestBody ReadStatusUpdateRequest request) {
        // log
        log.debug("ReadStatus 시간 업데이트 요청: {}", request.newLastReadAt());
        ReadStatusDto updatedReadStatus = readStatusService.update(readStatusId, request);
        log.info("ReadStatus 시간 업데이트 완료");
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(updatedReadStatus);
    }

    @GetMapping
    public ResponseEntity<List<ReadStatusDto>> findAllByUserId(
        @RequestParam("userId") UUID userId) {
        // log
        log.info("User의 readStatus 정보 조회");
        List<ReadStatusDto> readStatuses = readStatusService.findAllByUserId(userId);
        log.info("UserId ReadStatus 정보 조회 완료");
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(readStatuses);
    }
}
