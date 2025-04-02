package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.controller.readstatus.CreateReadStatusRequestDTO;
import com.sprint.mission.discodeit.dto.controller.readstatus.CreateReadStatusResponseDTO;
import com.sprint.mission.discodeit.dto.controller.readstatus.ReadStatusListDTO;
import com.sprint.mission.discodeit.dto.controller.readstatus.UpdateReadStatusResponseDTO;
import com.sprint.mission.discodeit.dto.service.readStatus.ReadStatusDTO;
import com.sprint.mission.discodeit.dto.service.readStatus.UpdateReadStatusDTO;
import com.sprint.mission.discodeit.dto.service.readStatus.UpdateReadStatusParam;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.service.ReadStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/readstatus")
public class ReadStatusController {
    private final ReadStatusService readStatusService;
    private final ReadStatusMapper readStatusMapper;

    @PostMapping
    public ResponseEntity<CreateReadStatusResponseDTO> createReadStatus(@RequestBody @Valid CreateReadStatusRequestDTO createReadStatusRequestDTO) {
        ReadStatusDTO readStatusDTO = readStatusService.create(readStatusMapper.toReadStatusParam(createReadStatusRequestDTO));
        return ResponseEntity.ok(readStatusMapper.toReadStatusResponseDTO(readStatusDTO));
    }

    @PutMapping("/{readStatusId}")
    public ResponseEntity<UpdateReadStatusResponseDTO> updateReadStatus(@PathVariable("readStatusId") UUID id) {
        UpdateReadStatusDTO updateReadStatusDTO = readStatusService.update(new UpdateReadStatusParam(id));
        return ResponseEntity.ok(readStatusMapper.toUpdateReadStatusResponseDTO(updateReadStatusDTO));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ReadStatusListDTO> getUserReadStatus(@PathVariable("userId") UUID userId) {
        List<ReadStatusDTO> readStatusDTOList = readStatusService.findAllByUserId(userId);
        return ResponseEntity.ok(new ReadStatusListDTO(readStatusDTOList));
    }
}
