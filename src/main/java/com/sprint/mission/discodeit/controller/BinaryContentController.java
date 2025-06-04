package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.dto.response.BinaryContentResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "BinaryContent", description = "첨부파일 API")
@RestController
@RequestMapping("/api/binaryContents")
@RequiredArgsConstructor
@Slf4j
public class BinaryContentController {

    private final BinaryContentService binaryContentService;

    @GetMapping("/{binaryContentId}")
    @Operation(summary = "첨부 파일 조회")
    @ApiResponse(responseCode = "200", description = "첨부 파일 조회 성공")
    @ApiResponse(responseCode = "404", description = "첨부 파일을 찾을 수 없음", content = @Content(examples = @ExampleObject(value = "Profile not found")))
    public ResponseEntity<BinaryContentResponseDto> find(
            @PathVariable @Parameter(description = "조회 할 첨부파일 ID") UUID binaryContentId
    ) {
        BinaryContentResponseDto binaryContent = binaryContentService.find(binaryContentId);
        return ResponseEntity.ok(binaryContent);
    }


    @GetMapping
    @Operation(summary = "여러 첨부 파일 조회")
    @ApiResponse(responseCode = "200", description = "첨부 파일 목록 조회 성공")
    public ResponseEntity<List<BinaryContentResponseDto>> findAll(
            @RequestParam @Parameter(description = "조회 할 첨부 파일 ID 목록") List<UUID> binaryContentIds
    ) {
        List<BinaryContentResponseDto> binaryContents = binaryContentService.findAll(binaryContentIds);
        return ResponseEntity.ok(binaryContents);
    }


    @GetMapping("/{binaryContentId}/download")
    @Operation(summary = "파일 다운로드")
    @ApiResponse(responseCode = "200", description = "파일 다운로드 성공")
    public ResponseEntity<?> download(
            @PathVariable UUID binaryContentId
    ) {
        log.debug("[BinaryContent Controller][download] Received download request: binaryContentId={}", binaryContentId);
        log.debug("[BinaryContent Controller][download] Calling binaryContentService.download()");
        ResponseEntity<?> downloadResponse = binaryContentService.download(binaryContentId);
        log.debug("[BinaryContent Controller][download] Download successfully: binaryContentId={}", binaryContentId);
        return downloadResponse;
    }
}
