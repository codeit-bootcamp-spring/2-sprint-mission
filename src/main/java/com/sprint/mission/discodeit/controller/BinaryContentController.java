package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.application.dto.binarycontent.BinaryContentResult;
import com.sprint.mission.discodeit.service.BinaryContentService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/binary-contents")
@RequiredArgsConstructor
public class BinaryContentController {

  private final BinaryContentService binaryContentService;

  @GetMapping("/{fileId}")
  public ResponseEntity<BinaryContentResult> downloadSingleFile(@PathVariable UUID fileId) {
    BinaryContentResult binaryContentResult = binaryContentService.getById(fileId);

    return ResponseEntity.ok()
        .body(binaryContentResult);
  }
}
