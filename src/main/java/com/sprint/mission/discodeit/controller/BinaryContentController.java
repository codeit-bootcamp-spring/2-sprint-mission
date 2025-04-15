package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "BinaryContent")
@RestController
@RequestMapping("/api/binaryContents")
@RequiredArgsConstructor
public class BinaryContentController {

  private final BinaryContentService binaryContentService;

  @GetMapping("/{binaryContentId}")
  public ResponseEntity<BinaryContent> get(@PathVariable UUID binaryContentId) {
    BinaryContent content = binaryContentService.find(binaryContentId);
    return ResponseEntity.ok(content);
  }

  @GetMapping
  public ResponseEntity<List<BinaryContent>> getMultiple(
      @RequestParam List<UUID> binaryContentIds) {
    List<BinaryContent> contents = binaryContentService.findAllByIdIn(binaryContentIds);
    return ResponseEntity.status(HttpStatus.OK).body(contents);
  }

  @GetMapping("/{binaryContentId}/download")
  public ResponseEntity<Resource> download(@PathVariable UUID binaryContentId) {
    BinaryContent content = binaryContentService.find(binaryContentId);

    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(content.getContentType()))
        .header(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + content.getFileName() + "\"")
        .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(content.getSize()))
        .body(new ByteArrayResource(content.getBytes()));
  }
}
