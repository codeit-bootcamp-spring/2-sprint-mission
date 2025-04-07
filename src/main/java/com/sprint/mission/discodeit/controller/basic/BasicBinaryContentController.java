package com.sprint.mission.discodeit.controller.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.basic.BinaryContentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "BinaryContent", description = "첨부 파일 API")
@RestController
@RequestMapping("/api/binaryContents")
@RequiredArgsConstructor
public class BasicBinaryContentController {

  private final BinaryContentService binaryContentService;
  Logger logger = LoggerFactory.getLogger(BasicBinaryContentController.class);

  @Operation(summary = "첨부 파일 조회")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "첨부 파일 조회 성공"),
      @ApiResponse(responseCode = "404", description = "첨부 파일을 찾을 수 없음")
  })
  @GetMapping("/{binaryContentId}")
  public ResponseEntity<BinaryContent> find(@PathVariable UUID binaryContentId) {
    BinaryContent response = binaryContentService.find(binaryContentId);
    logger.info("Successfully found binaryContent with id {}", binaryContentId);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "여러 첨부 파일 조회")
  @ApiResponse(responseCode = "200", description = "첨부 파일 목록 조회 성공")
  @GetMapping
  public ResponseEntity<List<BinaryContent>> findBinaryContentByIdIn(
      @Parameter(description = "조회할 첨부 파일 ID 목록")
      @RequestParam List<UUID> binaryContentIds) {
    List<BinaryContent> response = binaryContentService.findAllByIdIn(binaryContentIds);
    logger.info("Successfully found binaryContents with ids {}", binaryContentIds);
    return ResponseEntity.ok(response);
  }

}
