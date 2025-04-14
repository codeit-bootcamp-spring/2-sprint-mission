package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.controller.readstatus.CreateReadStatusRequestDTO;
import com.sprint.mission.discodeit.dto.controller.readstatus.CreateReadStatusResponseDTO;
import com.sprint.mission.discodeit.dto.controller.readstatus.UpdateReadStatusResponseDTO;
import com.sprint.mission.discodeit.dto.service.readStatus.ReadStatusDTO;
import com.sprint.mission.discodeit.dto.service.readStatus.UpdateReadStatusDTO;
import com.sprint.mission.discodeit.dto.service.readStatus.UpdateReadStatusParam;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.swagger.ReadStatusApi;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/readStatuses")
public class ReadStatusController implements ReadStatusApi {

  private final ReadStatusService readStatusService;
  private final ReadStatusMapper readStatusMapper;

  @Override
  @PostMapping
  public ResponseEntity<CreateReadStatusResponseDTO> createReadStatus(
      @RequestBody @Valid CreateReadStatusRequestDTO createReadStatusRequestDTO) {
    ReadStatusDTO readStatusDTO = readStatusService.create(
        readStatusMapper.toReadStatusParam(createReadStatusRequestDTO));
    CreateReadStatusResponseDTO createdReadStatus = readStatusMapper.toReadStatusResponseDTO(
        readStatusDTO);
    return ResponseEntity.ok(createdReadStatus);
  }


  @Override
  @PatchMapping("/{readStatusId}")
  public ResponseEntity<UpdateReadStatusResponseDTO> updateReadStatus(
      @PathVariable("readStatusId") UUID id,
      @RequestBody UpdateReadStatusParam request) {
    UpdateReadStatusDTO updateReadStatusDTO = readStatusService.update(
        id, request);
    UpdateReadStatusResponseDTO updatedReadStatus = readStatusMapper.toUpdateReadStatusResponseDTO(
        updateReadStatusDTO);
    return ResponseEntity.ok(updatedReadStatus);
  }

  @Override
  @GetMapping
  public ResponseEntity<List<ReadStatusDTO>> getUserReadStatus(
      @RequestParam("userId") UUID userId) {
    List<ReadStatusDTO> readStatuses = readStatusService.findAllByUserId(userId);
    return ResponseEntity.ok(readStatuses);
  }
}
