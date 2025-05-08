package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.BinaryContentApi;
import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/binaryContents")
public class BinaryContentController implements BinaryContentApi {

  private final BinaryContentService binaryContentService;
  private final BinaryContentMapper binaryContentMapper;

  @GetMapping(path = "{binaryContentId}")
  public ResponseEntity<BinaryContentDto> find(@PathVariable("binaryContentId") UUID binaryContentId) {
    log.info("▶▶ [API] Received request to find binaryContent - id: {}", binaryContentId);
    BinaryContent binaryContent = binaryContentService.find(binaryContentId);

    log.debug("▶▶ [API] Start converting to DTO - id: {}", binaryContentId);
    BinaryContentDto dto = binaryContentMapper.toDto(binaryContent);
    log.debug("▶▶ [API] Finished converting to DTO - id: {}", binaryContentId);

    log.info("◀◀ [API] Returning response for find binaryContent - id: {}", binaryContentId);
    return ResponseEntity.status(HttpStatus.OK).body(dto);
  }

  @GetMapping
  public ResponseEntity<List<BinaryContentDto>> findAllByIdIn(
          @RequestParam("binaryContentIds") List<UUID> binaryContentIds) {
    log.info("▶▶ [API] Received request to find multiple binaryContents - ids: {}", binaryContentIds);

    List<BinaryContent> binaryContents = binaryContentService.findAllByIdIn(binaryContentIds);

    log.debug("▶▶ [API] Start converting multiple to DTOs (requested: {})", binaryContentIds.size());
    List<BinaryContentDto> dtos = binaryContentMapper.toDtoList(binaryContents);
    log.debug("◀◀ [API] Finished converting multiple to DTOs (found: {})", dtos.size());

    log.info("◀◀ [API] Returning response for multiple binaryContents (requested: {}, found: {})",
            binaryContentIds.size(), dtos.size());
    return ResponseEntity.status(HttpStatus.OK).body(dtos);
  }

  @GetMapping(path = "{binaryContentId}/download")
  public ResponseEntity<?> download(@PathVariable("binaryContentId") UUID binaryContentId) {
    log.info("▶▶ [API] Received request to download file - id: {}", binaryContentId);
    ResponseEntity<?> response = binaryContentService.download(binaryContentId);
    log.debug("◀◀ [API] Finished creating download response - id: {}", binaryContentId);
    return response;
  }
}
