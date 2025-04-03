package com.sprint.mission.discodeit.adapter.inbound.content;

import com.sprint.mission.discodeit.adapter.inbound.content.response.BinaryContentResponse;
import com.sprint.mission.discodeit.core.content.entity.BinaryContent;
import com.sprint.mission.discodeit.core.content.usecase.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/api/binaryContents")
@RequiredArgsConstructor
public class BinaryController {

  private final BinaryContentService binaryContentService;

  @GetMapping
  public ResponseEntity<List<BinaryContentResponse>> findBinaryContent(
      @RequestParam("binaryContentIds") List<UUID> binaryContentIds) {
    List<BinaryContentResponse> list = new ArrayList<>();
    for (UUID binaryContentId : binaryContentIds) {
      BinaryContent content = binaryContentService.findById(binaryContentId);
      list.add(BinaryContentResponse.create(content));
    }

    return ResponseEntity.ok(list);
  }

  @GetMapping("/{binaryContentId}")
  public ResponseEntity<BinaryContentResponse> findBinaryContents(
      @PathVariable UUID binaryContentId) {

    return ResponseEntity.ok(
        BinaryContentResponse.create(binaryContentService.findById(binaryContentId)));
  }
}
