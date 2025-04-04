package com.sprint.mission.discodeit.adapter.inbound.content;

import com.sprint.mission.discodeit.core.content.entity.BinaryContent;
import com.sprint.mission.discodeit.core.content.usecase.BinaryContentService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/binaryContents")
@RequiredArgsConstructor
public class BinaryController {

  private final BinaryContentService binaryContentService;

  @GetMapping
  public ResponseEntity<List<BinaryContent>> findAllBinaryContent(
      @RequestParam("binaryContentIds") List<UUID> binaryContentIds) {
    List<BinaryContent> allByIdIn = binaryContentService.findAllByIdIn(binaryContentIds);

    return ResponseEntity.ok(allByIdIn);
  }

  @GetMapping("/{binaryContentId}")
  public ResponseEntity<BinaryContent> findBinaryContent(@PathVariable UUID binaryContentId) {

    return ResponseEntity.ok(binaryContentService.findById(binaryContentId));
  }
}
