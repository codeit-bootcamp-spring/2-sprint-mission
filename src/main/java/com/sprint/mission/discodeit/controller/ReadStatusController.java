package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.controller.readstatus.CreateReadStatusRequestDTO;
import com.sprint.mission.discodeit.dto.controller.readstatus.CreateReadStatusResponseDTO;
import com.sprint.mission.discodeit.dto.controller.readstatus.FindReadStatusResponseDTO;
import com.sprint.mission.discodeit.dto.controller.readstatus.UpdateReadStatusRequestDTO;
import com.sprint.mission.discodeit.dto.controller.readstatus.UpdateReadStatusResponseDTO;
import com.sprint.mission.discodeit.dto.service.readStatus.CreateReadStatusResult;
import com.sprint.mission.discodeit.dto.service.readStatus.FindReadStatusResult;
import com.sprint.mission.discodeit.dto.service.readStatus.UpdateReadStatusResult;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.swagger.ReadStatusApi;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/readStatuses")
public class ReadStatusController implements ReadStatusApi {

  private final ReadStatusService readStatusService;
  private final ReadStatusMapper readStatusMapper;

  @Override
  @PostMapping
  public ResponseEntity<CreateReadStatusResponseDTO> createReadStatus(
      @RequestBody @Valid CreateReadStatusRequestDTO createReadStatusRequestDTO) {
    log.info("ReadStatus create request (userId: {}, channelId: {}, lastReadAt: {})",
        createReadStatusRequestDTO.userId(), createReadStatusRequestDTO.channelId(),
        createReadStatusRequestDTO.lastReadAt());
    CreateReadStatusResult readStatusResult = readStatusService.create(
        readStatusMapper.toCreateReadStatusCommand(createReadStatusRequestDTO));
    CreateReadStatusResponseDTO createdReadStatus = readStatusMapper.toReadStatusResponseDTO(
        readStatusResult);
    return ResponseEntity.ok(createdReadStatus);
  }


  @Override
  @PatchMapping("/{readStatusId}")
  public ResponseEntity<UpdateReadStatusResponseDTO> updateReadStatus(
      @PathVariable("readStatusId") UUID id,
      @RequestBody UpdateReadStatusRequestDTO request) {
    log.info("ReadStatus create request (readStatusId: {}, lastReadAt: {})", id,
        request.newLastReadAt());
    UpdateReadStatusResult updateReadStatusResult = readStatusService.update(
        id, readStatusMapper.toUpdateReadStatusCommand(request));
    UpdateReadStatusResponseDTO updatedReadStatus = readStatusMapper.toUpdateReadStatusResponseDTO(
        updateReadStatusResult);
    return ResponseEntity.ok(updatedReadStatus);
  }

  @Override
  @GetMapping
  public ResponseEntity<List<FindReadStatusResponseDTO>> getUserReadStatus(
      @RequestParam("userId") UUID userId) {
    List<FindReadStatusResult> findReadStatusResults = readStatusService.findAllByUserId(userId);
    List<FindReadStatusResponseDTO> readStatuses = findReadStatusResults.stream()
        .map(readStatusMapper::toFindReadStatusResponseDTO)
        .toList();
    return ResponseEntity.ok(readStatuses);
  }
}
