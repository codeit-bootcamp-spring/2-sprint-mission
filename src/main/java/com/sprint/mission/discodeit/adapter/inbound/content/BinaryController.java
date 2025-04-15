package com.sprint.mission.discodeit.adapter.inbound.content;

import com.sprint.mission.discodeit.core.content.port.BinaryContentStoragePort;
import com.sprint.mission.discodeit.core.content.usecase.BinaryContentService;
import com.sprint.mission.discodeit.core.content.usecase.dto.BinaryContentResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Binary Content", description = "바이너리 데이터 관련 API")
@RestController
@RequestMapping("/api/binaryContents")
@RequiredArgsConstructor
public class BinaryController {

  private final BinaryContentStoragePort binaryContentStorage;
  private final BinaryContentService binaryContentService;

  @GetMapping
  public ResponseEntity<List<BinaryContentResult>> findAllBinaryContent(
      @RequestParam("binaryContentIds") List<UUID> binaryContentIds) {
    List<BinaryContentResult> allByIdIn = binaryContentService.findAllByIdIn(binaryContentIds);

    return ResponseEntity.ok(allByIdIn);
  }

  @GetMapping("/{binaryContentId}")
  public ResponseEntity<BinaryContentResult> findBinaryContent(@PathVariable UUID binaryContentId) {

    return ResponseEntity.ok(binaryContentService.findById(binaryContentId));
  }

  @GetMapping("/{binaryContentId}/download")
  public ResponseEntity<?> download(@PathVariable UUID binaryContentId) {
    BinaryContentResult result = binaryContentService.findById(binaryContentId);
    return binaryContentStorage.download(result);
  }
}
