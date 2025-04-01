package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.basic.BasicReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/readStatus")
public class ReadStatusController {

    private final BasicReadStatusService readStatusService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<ReadStatus> createReadStatus(
            @RequestBody ReadStatusCreateRequest readStatusCreateRequest) {

        ReadStatus createdReadStatus = readStatusService.create(readStatusCreateRequest);

        return new ResponseEntity<>(createdReadStatus, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseEntity<ReadStatus> updateReadStatus(
            @RequestParam UUID readStatusId,
            @RequestBody ReadStatusUpdateRequest readStatusUpdateRequest) {

        ReadStatus updatedReadStatus = readStatusService.update(readStatusId, readStatusUpdateRequest);

        return new ResponseEntity<>(updatedReadStatus, HttpStatus.OK);
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ResponseEntity<List<ReadStatus>> getReadStatusByUser(
            @RequestParam UUID userId) {

        List<ReadStatus> readStatuses = readStatusService.findAllByUserId(userId);

        return new ResponseEntity<>(readStatuses, HttpStatus.OK);
    }
}
