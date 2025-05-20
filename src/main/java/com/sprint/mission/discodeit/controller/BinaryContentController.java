package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.common.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
  public ResponseEntity<BinaryContent> find(
      @PathVariable @NotNull(message = "Binary Content ID는 필수입니다.") UUID binaryContentId) {
    BinaryContent content = binaryContentService.find(binaryContentId);
    return ResponseEntity.ok(content);
  }

  @GetMapping
  public ResponseEntity<List<BinaryContent>> findAll(
      @RequestParam @NotNull(message = "Binary Content ID 목록은 필수입니다.") List<UUID> binaryContentIds) {
    List<BinaryContent> contents = binaryContentService.findAllByIdIn(binaryContentIds);
    return ResponseEntity.ok(contents);
  }

  @GetMapping("/{binaryContentId}/download")
  public ResponseEntity<?> download(
      @PathVariable @NotNull(message = "Binary Content ID는 필수입니다.") UUID binaryContentId) {
    return binaryContentService.download(binaryContentId);
  }

}
