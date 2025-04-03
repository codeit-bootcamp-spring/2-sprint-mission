package com.sprint.mission.discodeit.controller.basic;

import com.sprint.mission.discodeit.dto.request.ReadStatusRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.basic.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/readStatus")
@RequiredArgsConstructor
public class BasicReadStatusController {

    private final ReadStatusService readStatusService;
    Logger logger = LoggerFactory.getLogger(BasicReadStatusController.class);

    @PostMapping("/create")
    public ResponseEntity<ReadStatus> create(@RequestBody ReadStatusRequest request) {
        ReadStatus response = readStatusService.create(request);
        logger.info("Succesfully created readStatus: {}", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{readStatusId}/update")
    public ResponseEntity<ReadStatus> update(@PathVariable String readStatusId, @RequestBody ReadStatusUpdateRequest request) {
        ReadStatus response = readStatusService.update(UUID.fromString(readStatusId), request);
        logger.info("Succesfully updated readStatus: {}", response);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}/findAll")
    public ResponseEntity<List<ReadStatus>> findAllByUserId(@PathVariable String userId){
        List<ReadStatus> response = readStatusService.findAllByUserId(UUID.fromString(userId));
        logger.info("Succesfully findAllByUserId: {}", response);
        return ResponseEntity.ok(response);
    }
}
