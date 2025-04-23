package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/binaryContents")
@Tag(name = "BinaryContent", description = "첨부 파일 API")
public class BinaryContentController {

    private final BinaryContentService binaryContentService;
    private final BinaryContentStorage binaryContentStorage;
    private final BinaryContentMapper binaryContentMapper;

    @Operation(summary = "여러 첨부 파일 조회")
    @GetMapping
    public ResponseEntity<List<BinaryContentDto>> getBinaryContents(@RequestParam List<UUID> binaryContentIds) {
        List<BinaryContentDto> binaryContentDtos = binaryContentService.findAllByIdIn(binaryContentIds).stream()
                .toList();

        return ResponseEntity.ok(binaryContentDtos);
    }

    @Operation(summary = "첨부 파일 조회")
    @GetMapping("/{binaryContentId}")
    public ResponseEntity<BinaryContentDto> getBinaryContentById(@PathVariable UUID binaryContentId) {
        BinaryContentDto binaryContentDto = binaryContentService.findById(binaryContentId);

        return ResponseEntity.ok(binaryContentDto);
    }

    @Operation(summary = "파일 다운로드")
    @GetMapping("/{binaryContentId}/download")
    public ResponseEntity<?> downloadBinaryContent(@PathVariable UUID binaryContentId) {
        BinaryContentDto binaryContent = binaryContentService.findById(binaryContentId);

        return binaryContentStorage.download(binaryContent);
    }
}
