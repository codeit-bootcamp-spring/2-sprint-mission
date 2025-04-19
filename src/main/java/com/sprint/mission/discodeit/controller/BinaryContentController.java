package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/binaryContents")
public class BinaryContentController {

  private final BinaryContentService binaryContentService;
  private final BinaryContentStorage binaryContentStorage;

  @GetMapping("/{binaryContentId}")
  public ResponseEntity<BinaryContentDto> get(@PathVariable UUID binaryContentId) {
    BinaryContentDto file = binaryContentService.findById(binaryContentId);
    return ResponseEntity.ok(file);
  }

  @GetMapping
  public ResponseEntity<List<BinaryContentDto>> getFiles(@RequestParam List<UUID> ids) {
    List<BinaryContentDto> files = binaryContentService.findAllByIdIn(ids);
    return ResponseEntity.ok(files);
  }

  @GetMapping("/{binaryContentId}/download")
  public ResponseEntity<?> download(
      @PathVariable UUID binaryContentId,
      @RequestParam(required = false) String fileName
  ) {
    BinaryContentDto dto = new BinaryContentDto(binaryContentId, fileName, null, null);
    return binaryContentStorage.download(dto);
  }


}
