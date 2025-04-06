package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/binaryContents")
@RequiredArgsConstructor
@Tag(name = "BinaryContent", description = "첨부 파일 API")
public class BinaryContentController {

  private final BinaryContentService binaryContentService;

  @GetMapping("/{binaryContentId}")
  @Operation(summary = "첨부 파일 조회", operationId = "find")
  @Parameters(value = {
      @Parameter(
          name = "binaryContentId",
          in = ParameterIn.PATH,
          description = "조회할 첨부 파일 ID",
          required = true,
          schema = @Schema(type = "string", format = "uuid")
      )
  })
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "첨부 파일 조회 성공",
          content = @Content(
              mediaType = "*/*",
              schema = @Schema(implementation = BinaryContent.class)
          )
      ),
      @ApiResponse(
          responseCode = "404",
          description = "첨부 파일을 찾을 수 없음",
          content = @Content(
              mediaType = "*/*",
              examples = @ExampleObject("BinaryContent with id {binaryContentId} not found")
          )
      )
  })
  public ResponseEntity<BinaryContent> find(
      @PathVariable("binaryContentId") UUID binaryContentId
  ) {
    binaryContentService.findById(binaryContentId);
    return ResponseEntity.status(HttpStatus.OK).body(
        binaryContentService.findById(binaryContentId));
  }

  @GetMapping("")
  @Operation(summary = "여러 첨부 파일 조회", operationId = "findAllByIdIn")
  @Parameters(value = {
      @Parameter(
          name = "binaryContentIds",
          in = ParameterIn.QUERY,
          description = "조회할 첨부 파일 ID 목록",
          required = true,
          array = @ArraySchema(schema = @Schema(type = "string", format = "uuid"))
      )
  })
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "첨부 파일 목록 조회 성공",
          content = @Content(
              mediaType = "*/*",
              array = @ArraySchema(schema = @Schema(implementation = BinaryContent.class))
          )
      )
  })
  public ResponseEntity<List<BinaryContent>> findByIdIn(
      @RequestParam("binaryContentIds") List<UUID> binaryContentIdList
  ) {
    List<BinaryContent> result = binaryContentService.findByIdIn(binaryContentIdList);
    return ResponseEntity.status(HttpStatus.OK).body(result);
  }
}
