package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.file.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/binaryContents")
@RequiredArgsConstructor
public class BinaryContentController {

    private final BinaryContentService binaryContentService;
    private final BinaryContentStorage binaryContentStorage;

    @Operation(summary = "파일 단건 조회")
    @GetMapping("/{binaryContentId}")
    public ResponseEntity<BinaryContentDto> findById(@PathVariable UUID binaryContentId) {
        log.info("파일 단건 조회 API 호출 - fileId: {} ", binaryContentId);

        BinaryContentDto dto = binaryContentService.find(binaryContentId);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "파일 다중 조회")
    @GetMapping
    public ResponseEntity<List<BinaryContent>> findAll(
        @RequestParam("binaryContentIds") List<UUID> ids) {

        log.info("파일 다중 조회 API 호출 - fileIds: {}", ids);
        return ResponseEntity.ok(binaryContentService.findAllByIdIn(ids));
    }

    @Operation(summary = "파일 다운로드")
    @GetMapping("/{binaryContentId}/download")
    public ResponseEntity<?> download(@PathVariable UUID binaryContentId) {

        log.info("파일 다운로드 API 호출 - fileId: {}", binaryContentId);
        BinaryContentDto dto = binaryContentService.find(binaryContentId);
        return binaryContentStorage.download(dto);
    }
}
