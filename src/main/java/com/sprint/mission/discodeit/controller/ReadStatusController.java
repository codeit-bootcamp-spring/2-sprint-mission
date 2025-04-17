package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.ReadStatusDto;
import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.service.ReadStatusService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "ReadStatus")
@RestController
@RequestMapping("/api/readStatuses")
@RequiredArgsConstructor
public class ReadStatusController {

  private final ReadStatusService readStatusService;
  private final ReadStatusMapper readStatusMapper;

  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<ReadStatusDto> create(@RequestBody ReadStatusCreateRequest request) {
    ReadStatus readStatus = readStatusService.create(request);
    ReadStatusDto readStatusDto = readStatusMapper.toDto(readStatus);
    return ResponseEntity.status(HttpStatus.CREATED).body(readStatusDto);
  }

  @RequestMapping(value = "/{readStatusId}", method = RequestMethod.PATCH)
  public ResponseEntity<ReadStatusDto> update(@PathVariable UUID readStatusId,
      @RequestBody ReadStatusUpdateRequest request) {
    ReadStatus readStatus = readStatusService.update(readStatusId, request);
    ReadStatusDto readStatusDto = readStatusMapper.toDto(readStatus);
    return ResponseEntity.status(HttpStatus.OK).body(readStatusDto);
  }

  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<List<ReadStatusDto>> get(@RequestParam UUID userId) {
    List<ReadStatusDto> readStatusDtos = readStatusService.findAllByUserId(userId)
        .stream()
        .map(readStatusMapper::toDto)
        .toList();

    return ResponseEntity.status(HttpStatus.OK).body(readStatusDtos);
  }
}
