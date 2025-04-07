package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.application.dto.binarycontent.BinaryContentRequest;
import com.sprint.mission.discodeit.application.dto.binarycontent.BinaryContentResult;
import com.sprint.mission.discodeit.service.BinaryContentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/binaryContents")
@Tag(name = "BinaryContent", description = "바이너리 컨텐츠 관련 API")
@RequiredArgsConstructor
public class BinaryContentController {

    private final BinaryContentService binaryContentService;

    @Operation(
            summary = "바이너리 컨텐츠 생성"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "바이너리 컨텐츠 생성 성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 오류")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BinaryContentResult> create(
            @Parameter(description = "바이너리 이미지", required = true)
            @RequestPart MultipartFile multipartFile) {

        BinaryContentRequest binaryContentRequest = BinaryContentRequest.fromMultipartFile(multipartFile);
        BinaryContentResult binaryContentResult = binaryContentService.createProfileImage(binaryContentRequest);

        return ResponseEntity.ok(binaryContentResult);
    }

    @Operation(
            summary = "바이너리 컨텐츠 다수 조회",
            description = "여러개의 바이너리 컨텐츠를 조회"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "바이너리 컨텐츠 조회 성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 오류")
    })
    @GetMapping
    public ResponseEntity<List<BinaryContentResult>> getByIdIn(
            @Parameter(description = "바이너리 컨텐츠 IDs", required = true)
            @RequestParam(value = "binaryContentIds") List<UUID> binaryContentIds) {
        List<BinaryContentResult> binaryContentResults = binaryContentService.getByIdIn(binaryContentIds);

        return ResponseEntity.ok(binaryContentResults);
    }

    @Operation(
            summary = "바이너리 컨텐츠 조회",
            description = "단일 바이너리 컨텐츠 조회"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "바이너리 컨텐츠 조회 성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 오류")
    })
    @GetMapping("/{binaryContentId}")
    public ResponseEntity<BinaryContentResult> getById(
            @Parameter(description = "바이너리 컨텐츠 ID", required = true)
            @PathVariable UUID binaryContentId) {

        BinaryContentResult binaryContentResult = binaryContentService.getById(binaryContentId);

        return ResponseEntity.ok()
                .body(binaryContentResult);
    }

    @Operation(
            summary = "바이너리 컨텐츠 삭제"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "바이너리 컨텐츠 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 오류")
    })
    @DeleteMapping("/{binaryContentId}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "바이너리 컨텐츠 ID", required = true)
            @PathVariable UUID binaryContentId) {
        binaryContentService.delete(binaryContentId);

        return ResponseEntity.noContent().build();
    }
}
