package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.util.LogMapUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/binaryContents")
@Tag(name = "BinaryContent", description = "첨부 파일 API")
public class BinaryContentController {

  private final BinaryContentService binaryContentService;
  private static final Logger log = LoggerFactory.getLogger(BinaryContentController.class);

  @GetMapping("/{binaryContentKey}")
  public ResponseEntity<BinaryContent> read(@PathVariable UUID binaryContentKey) {
    BinaryContent content = binaryContentService.find(binaryContentKey);
    log.info("{}", LogMapUtil.of("action", "read")
        .add("content", content));
    return ResponseEntity.ok(content);
  }

  @GetMapping
  public ResponseEntity<List<BinaryContent>> readAll(@RequestParam List<UUID> binaryContentKeys) {
    List<BinaryContent> contents = binaryContentService.findAllByKeys(binaryContentKeys);
    log.info("{}", LogMapUtil.of("action", "readAll")
        .add("contents", contents));
    return ResponseEntity.ok(contents);
  }
}
