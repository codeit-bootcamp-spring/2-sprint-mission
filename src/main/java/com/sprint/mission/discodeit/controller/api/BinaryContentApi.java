package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.controller.dto.BinaryContentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;

@Tag(name = "BinaryContent", description = "첨부 파일 API")
public interface BinaryContentApi {

  // 파일 조회
  @Operation(summary = "첨부 파일 조회")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "첨부 파일 조회 성공",
          content = @Content(mediaType = "*/*", schema = @Schema(implementation = BinaryContentResponse.class))),
      @ApiResponse(responseCode = "404", description = "첨부 파일을 찾을 수 없음",
          content = @Content(mediaType = "*/*", examples =
          @ExampleObject(value = "{binaryContentId}에 해당하는 BinaryContent를 찾을 수 없음")))
  })
  ResponseEntity<BinaryContentResponse> find(
      @Parameter(description = "조회할 첨부 파일 ID") UUID binaryContentId);

  // 여러 파일 조회
  @Operation(summary = "여러 첨부 파일 조회")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "첨부 파일 목록 조회 성공",
          content = @Content(mediaType = "*/*", array = @ArraySchema(schema = @Schema(implementation = BinaryContentResponse.class)))),
      @ApiResponse(responseCode = "404", description = "첨부 파일을 찾을 수 없음",
          content = @Content(mediaType = "*/*", examples =
          @ExampleObject(value = "{binaryContentId}에 해당하는 BinaryContent를 찾을 수 없음")))
  })
  ResponseEntity<List<BinaryContentResponse>> findAll(
      @Parameter(description = "조회할 첨부 파일 ID 목록") List<UUID> binaryContentIds);
}
