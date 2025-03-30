package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
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
@RequestMapping("/readStatus")
public class ReadStatusController {

    private final ReadStatusService readStatusService;
    private static final Logger log = LoggerFactory.getLogger(ReadStatusController.class);

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<?> create(@RequestBody ReadStatusCreateRequest request) {
        ReadStatus readStatus = readStatusService.create(request);
        log.info("수신 정보 생성 완료: {}", readStatus);

        return ResponseEntity.status(HttpStatus.CREATED).body(readStatus);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseEntity<?> update(@RequestBody ReadStatusUpdateRequest request) {
        ReadStatus updated = readStatusService.update(request);
        log.info("수신 정보 수정 완료: {}", updated);

        return ResponseEntity.ok(updated);
    }

    @RequestMapping(value = "/readAllByUser", method = RequestMethod.GET)
    public ResponseEntity<?> readAllByUser(@RequestParam UUID userKey) {
        List<ReadStatus> list = readStatusService.findAllByUserKey(userKey);
        log.info("조회된 수신 정보: {}", list);

        return ResponseEntity.ok(list);
    }
}
