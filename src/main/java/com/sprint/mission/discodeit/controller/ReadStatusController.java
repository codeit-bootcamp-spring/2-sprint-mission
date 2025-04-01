package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.dto.readstatusdto.ReadStatusCreateDto;
import com.sprint.mission.discodeit.service.dto.readstatusdto.ReadStatusFindDto;
import com.sprint.mission.discodeit.service.dto.readstatusdto.ReadStatusUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/readStatus")
@RequiredArgsConstructor
public class ReadStatusController {

    private final ReadStatusService readStatusService;

    @PostMapping("/create")
    public ResponseEntity<ReadStatus> create(
            @RequestBody ReadStatusCreateDto readStatusCreateRequest
    ) {
        ReadStatus createReadStatus = readStatusService.create(readStatusCreateRequest);
        return ResponseEntity.ok(createReadStatus);
    }


    @PutMapping("/update")
    public ResponseEntity<ReadStatus> update(
            @RequestBody ReadStatusUpdateDto readStatusUpdateRequest
    ) {
        ReadStatus updateReadStatus = readStatusService.update(readStatusUpdateRequest);
        return ResponseEntity.ok(updateReadStatus);
    }


    @GetMapping("/findAllByUserId")
    public ResponseEntity<List<ReadStatus>> findAllByUserId(
            @RequestBody ReadStatusFindDto readStatusFindRequest
    ) {
        List<ReadStatus> findAllByUserId = readStatusService.findAllByUserId(readStatusFindRequest);
        return ResponseEntity.ok(findAllByUserId);
    }
}
