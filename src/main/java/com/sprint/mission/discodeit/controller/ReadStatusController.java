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
            @RequestPart("readStatusInfo") ReadStatusCreateDto readStatusCreateReq
    ) {
        ReadStatus createReadStatus = readStatusService.create(readStatusCreateReq);
        return ResponseEntity.ok(createReadStatus);
    }


    @PutMapping("/update")
    public ResponseEntity<ReadStatus> update(
            @RequestPart("readStatusInfo")ReadStatusUpdateDto readStatusUpdateReq
    ) {
        ReadStatus updateReadStatus = readStatusService.update(readStatusUpdateReq);
        return ResponseEntity.ok(updateReadStatus);
    }


    @GetMapping("/findAllByUserId")
    public ResponseEntity<List<ReadStatus>> findAllByUserId(
            @RequestPart("readStatusFindInfo")ReadStatusFindDto readStatusFindReq
    ) {
        List<ReadStatus> findAllByUserId = readStatusService.findAllByUserId(readStatusFindReq);
        return ResponseEntity.ok(findAllByUserId);
    }
}
