package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.dto.BinaryContentResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/binaryContents")
@RequiredArgsConstructor
@Tag(name = "BinaryContent", description = "첨부 파일 API")
public class BinaryContentController {

  private final BinaryContentService binaryContentService;

  // 파일 조회
  @Operation(summary = "첨부 파일 조회")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "첨부 파일 조회 성공",
          content = @Content(mediaType = "*/*", schema = @Schema(implementation = BinaryContentResponse.class))),
      @ApiResponse(responseCode = "404", description = "첨부 파일을 찾을 수 없음",
          content = @Content(mediaType = "*/*", examples =
          @ExampleObject(value = "{binaryContentId}에 해당하는 BinaryContent를 찾을 수 없음")))
  })
  @RequestMapping(value = "/{binaryContentId}", method = RequestMethod.GET)
  public ResponseEntity<BinaryContentResponse> find(
      @PathVariable @Parameter(description = "조회할 첨부 파일 ID") UUID binaryContentId) {
    BinaryContent binaryContent = binaryContentService.findById(binaryContentId);
    return ResponseEntity.ok(BinaryContentResponse.of(binaryContent));
  }

  // 여러 파일 조회
  @Operation(summary = "여러 첨부 파일 조회")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "첨부 파일 목록 조회 성공",
          content = @Content(mediaType = "*/*", array = @ArraySchema(schema = @Schema(implementation = BinaryContentResponse.class)))),
      @ApiResponse(responseCode = "404", description = "첨부 파일을 찾을 수 없음",
          content = @Content(mediaType = "*/*", examples =
          @ExampleObject(value = "{binaryContentId}에 해당하는 BinaryContent를 찾을 수 없음")))
  })
  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<List<BinaryContentResponse>> findAll(
      @RequestParam @Parameter(description = "조회할 첨부 파일 ID 목록") List<UUID> binaryContentIds) {
    List<BinaryContent> binaryContents = binaryContentService.findAllByIdIn(binaryContentIds);
    List<BinaryContentResponse> response = new ArrayList<>();
    for (BinaryContent binaryContent : binaryContents) {
      response.add(BinaryContentResponse.of(binaryContent));
    }
    return ResponseEntity.ok(response);
  }
}