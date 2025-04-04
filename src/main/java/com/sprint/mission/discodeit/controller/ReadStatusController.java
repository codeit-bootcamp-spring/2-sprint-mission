package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/read-status")
public class ReadStatusController {
    private final ReadStatusService readStatusService;

    public ReadStatusController(ReadStatusService readStatusService) {
        this.readStatusService = readStatusService;
    }

    @PostMapping
    public ReadStatus create(@RequestBody ReadStatusCreateRequest request) {
        return readStatusService.create(request);
    }

    @PutMapping("/{readStatusId}")
    public ReadStatus update(
            @PathVariable UUID readStatusId,
            @RequestBody ReadStatusUpdateRequest request
    ) {
        return readStatusService.update(readStatusId, request);
    }

    @GetMapping("/user/{userId}")
    public List<ReadStatus> findAllByUserId(@PathVariable UUID userId) {
        return readStatusService.findAllByUserId(userId);
    }

    @GetMapping("/{readStatusId}")
    public ReadStatus findById(@PathVariable UUID readStatusId) {
        return readStatusService.find(readStatusId);
    }
}