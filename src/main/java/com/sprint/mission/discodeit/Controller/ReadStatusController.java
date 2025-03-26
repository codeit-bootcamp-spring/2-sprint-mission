package com.sprint.mission.discodeit.Controller;

import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/readstatus")
@RequiredArgsConstructor
public class ReadStatusController {
    private final ReadStatusService readStatusService;

    @PostMapping
    public ResponseEntity<ReadStatus> createReadStatus(@RequestBody ReadStatusCreateRequest readStatusCreateRequest) {
        ReadStatus createReadStatus = readStatusService.create(readStatusCreateRequest);
        return ResponseEntity.ok(createReadStatus);
    }

    @PutMapping("/{readStatusId}")
    public ResponseEntity<ReadStatus> updateReadStatus(@PathVariable UUID readStatusId, @RequestBody ReadStatusUpdateRequest readStatusUpdateRequest) {
        ReadStatus updateReadStatus = readStatusService.update(readStatusId, readStatusUpdateRequest);
        return ResponseEntity.ok(updateReadStatus);
    }





}
