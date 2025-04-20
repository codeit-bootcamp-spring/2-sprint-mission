package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.common.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/binaryContents")
@RequiredArgsConstructor
public class BinaryContentController {

  private final BinaryContentService binaryContentService;

  @GetMapping("/{binaryContentId}")
  public ResponseEntity<BinaryContent> find(@PathVariable UUID binaryContentId) {
    BinaryContent content = binaryContentService.find(binaryContentId);
    return ResponseEntity.ok(content);
  }

  @GetMapping
  public ResponseEntity<List<BinaryContent>> findAll(
      @RequestParam List<UUID> binaryContentIds) {
    List<BinaryContent> contents = binaryContentService.findAllByIdIn(binaryContentIds);
    return ResponseEntity.ok(contents);
  }

  @GetMapping("/{binaryContentId}/download")
  public ResponseEntity<?> download(@PathVariable UUID binaryContentId) {
    return binaryContentService.download(binaryContentId);
  }

}
