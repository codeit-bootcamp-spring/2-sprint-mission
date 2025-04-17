package com.sprint.mission.discodeit.swagger;

import com.sprint.mission.discodeit.dto.controller.binarycontent.FindBinaryContentResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;


@Tag(name = "Binary-Content-Controller", description = "BinaryContent 관련 API")
public interface BinaryContentApi {

  @Operation(summary = "BinaryContent 단건 조회",
      description = "binaryContentId에 해당하는 BinaryContent를 조회합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "BinaryContent 조회 성공"),
          @ApiResponse(responseCode = "404", description = "binaryContentId에 해당하는 BinaryContnet가 존재하지 않음")
      })
  ResponseEntity<FindBinaryContentResponseDTO> getBinaryContent(UUID id);

  @Operation(summary = "BinaryContent 다건 조회",
      description = "binaryContentIds에 해당하는 BinaryContent들을 조회합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "BinaryContent 다건 조회 성공"),
          @ApiResponse(responseCode = "404", description = "binaryContentId에 해당하는 BinaryContnet가 존재하지 않음")
      })
  ResponseEntity<List<FindBinaryContentResponseDTO>> getBinaryContentAllIn(
      List<UUID> binaryContentIds);

}
