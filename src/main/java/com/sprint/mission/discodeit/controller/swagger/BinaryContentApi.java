package com.sprint.mission.discodeit.controller.swagger;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
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
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "BinaryContent", description = "첨부 파일 API")
public interface BinaryContentApi {
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
    ResponseEntity<BinaryContentDto> find(
        @PathVariable("binaryContentId") UUID binaryContentId
    );

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
    ResponseEntity<List<BinaryContentDto>> findByIdIn(
        @RequestParam("binaryContentIds") List<UUID> binaryContentIdList
    );

    @Operation(summary = "파일 다운로드", operationId = "download")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "파일 다운로드 성공",
            content = @Content(
                mediaType = "*/*",
                schema = @Schema(type = "string", format = "binary")
            )
        )
    })
    ResponseEntity<Resource> binaryContentDownload(
        @PathVariable("binaryContentId") UUID binaryContentId
    );
}
