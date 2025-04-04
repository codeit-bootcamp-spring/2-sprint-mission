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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Read-Status-Controller", description = "ReadStatus 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/readStatuses")
public class ReadStatusController {

  private final ReadStatusService readStatusService;
  private final ReadStatusMapper readStatusMapper;

  @Operation(summary = "읽음상태 생성",
      description = "userId와 channelId를 기반으로 읽음상태를 생성합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "읽음상태 생성 성공"),
          @ApiResponse(responseCode = "404", description = "userId, channelId에 해당하는 리소스가 존재하지 않음")
      })
  @PostMapping
  public ResponseEntity<CreateReadStatusResponseDTO> createReadStatus(
      @RequestBody @Valid CreateReadStatusRequestDTO createReadStatusRequestDTO) {
    ReadStatusDTO readStatusDTO = readStatusService.create(
        readStatusMapper.toReadStatusParam(createReadStatusRequestDTO));
    CreateReadStatusResponseDTO createdReadStatus = readStatusMapper.toReadStatusResponseDTO(
        readStatusDTO);
    return ResponseEntity.ok(createdReadStatus);
  }

  @Operation(summary = "읽음상태 수정",
      description = "readStatusId에 해당하는 읽음상태를 수정합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "읽음상태 수정 성공"),
          @ApiResponse(responseCode = "404", description = "readStatusId에 해당하는 ReadStatus가 존재하지 않음")
      })
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

  @Operation(summary = "유저의 읽음상태 조회",
      description = "userId에 해당하는 읽음상태들을 조회합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "유저의 읽음상태 조회 성공")
      })
  @GetMapping
  public ResponseEntity<List<ReadStatusDTO>> getUserReadStatus(
      @RequestParam("userId") UUID userId) {
    List<ReadStatusDTO> readStatuses = readStatusService.findAllByUserId(userId);
    return ResponseEntity.ok(readStatuses);
  }
}
