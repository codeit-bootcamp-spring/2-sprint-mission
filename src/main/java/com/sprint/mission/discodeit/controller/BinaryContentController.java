package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.file.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/binaryContents")
@RequiredArgsConstructor
public class BinaryContentController {

    private final BinaryContentService binaryContentService;
    private final BinaryContentMapper binaryContentMapper;
    private final BinaryContentStorage binaryContentStorage;

    @Operation(summary = "파일 단건 조회")
    @GetMapping("/{binaryContentId}")
    public ResponseEntity<BinaryContent> findById(@PathVariable UUID binaryContentId) {
        return binaryContentService.find(binaryContentId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "파일 다중 조회")
    @GetMapping
    public ResponseEntity<List<BinaryContent>> findAll(
        @RequestParam("binaryContentIds") List<UUID> ids) {
        return ResponseEntity.ok(binaryContentService.findAllByIdIn(ids));
    }

    @Operation(summary = "파일 다운로드")
    @GetMapping("/{binaryContentId}/download")
    public ResponseEntity<?> download(@PathVariable UUID binaryContentId) {
        BinaryContent binaryContent = binaryContentService.find(binaryContentId)
            .orElseThrow(() -> new IllegalArgumentException("파일을 찾을 수 없습니다."));

        BinaryContentDto dto = binaryContentMapper.toDto(binaryContent);
        return binaryContentStorage.download(dto);
    }
}
