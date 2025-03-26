package com.sprint.mission.discodeit.Controller;

import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
