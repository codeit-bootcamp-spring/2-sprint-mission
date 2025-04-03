package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/binaryContent")
@Tag(name = "BinaryContent", description = "바이너리 콘텐츠 관리 API")
public class BinaryContentController {
    private final BinaryContentService binaryContentService;

    @Operation(summary = "바이너리 콘텐츠 조회", description = "특정 ID를 가진 바이너리 콘텐츠를 조회합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "조회 성공",
            content = @Content(schema = @Schema(implementation = BinaryContent.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "바이너리 콘텐츠 없음",
            content = @Content(examples = @ExampleObject(value = "BinaryContent with ID {binaryContentId} not found"))
    )
    @GetMapping("/{binaryContentId}")
    public ResponseEntity<BinaryContent> find(
            @Parameter(description = "조회할 바이너리 콘텐츠 ID", required = true)
            @PathVariable("binaryContentId") UUID binaryContentId) {
        BinaryContent binaryContent = binaryContentService.find(binaryContentId);
        return ResponseEntity.ok(binaryContent);
    }

    @Operation(summary = "다건 바이너리 콘텐츠 조회", description = "여러 ID를 기반으로 바이너리 콘텐츠를 조회합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "조회 성공",
            content = @Content(schema = @Schema(type = "array", implementation = BinaryContent.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청 데이터",
            content = @Content(examples = @ExampleObject(value = "Invalid request data"))
    )
    @GetMapping
    public ResponseEntity<List<BinaryContent>> findAllByIdIn(
            @Parameter(description = "조회할 바이너리 콘텐츠 ID 목록", required = true)
            @RequestParam("binaryContentIds") List<UUID> binaryContentIds) {
        List<BinaryContent> binaryContents = binaryContentService.findAllByIdIn(binaryContentIds);
        return ResponseEntity.ok(binaryContents);
    }
}
