package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.create.ReadStatusCreateRequestDTO;
import com.sprint.mission.discodeit.dto.update.ReadStatusUpdateRequestDTO;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/readstatus")
public class ReadStatusController {
    private final ReadStatusService readStatusService;

    @PostMapping("/create")
    public ResponseEntity<String> createReadStatus(@RequestBody ReadStatusCreateRequestDTO requestDTO) {
        readStatusService.create(requestDTO);

        return ResponseEntity.ok("success");
    }

    @PutMapping("/update/{channelId}")
    public ResponseEntity<String> update(@PathVariable UUID channelId,@RequestBody ReadStatusUpdateRequestDTO requestDTO) {
        readStatusService.update(channelId, requestDTO);
        return ResponseEntity.ok("success");
    }

    @GetMapping("/find/{userId}")
    public ResponseEntity<String> find(@PathVariable UUID userId) {
        ReadStatus readStatus = readStatusService.findByUserId(userId);
        if (readStatus != null) {
            return ResponseEntity.ok("success");
        } else {
            return ResponseEntity.status(404).body("fail");
        }
    }

}
