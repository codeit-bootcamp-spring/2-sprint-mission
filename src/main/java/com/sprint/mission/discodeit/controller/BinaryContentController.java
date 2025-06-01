package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentStorage;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/binaryContents")

public class BinaryContentController {

    private final BinaryContentService binaryContentService;
    private final BinaryContentStorage s3binaryContentStorage;
    private final BinaryContentMapper binaryContentMapper;

    @GetMapping(path = "{binaryContentId}")
    public ResponseEntity<BinaryContent> find(
        @PathVariable("binaryContentId") UUID binaryContentId) {
        BinaryContent binaryContent = binaryContentService.find(binaryContentId);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(binaryContent);
    }

    @GetMapping
    public ResponseEntity<List<BinaryContent>> findAllByIdIn(
        @RequestParam("binaryContentIds") List<UUID> binaryContentIds) {
        List<BinaryContent> binaryContents = binaryContentService.findAllByIdIn(binaryContentIds);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(binaryContents);
    }

    @GetMapping("/{binaryContentId}/download")
    public ResponseEntity<Void> download(
        @PathVariable("binaryContentId") UUID binaryContentId) {
        binaryContentService.find(binaryContentId);
        s3binaryContentStorage.download(binaryContentId);
        return null;
    }

    @PostMapping("/upload")
    public ResponseEntity<BinaryContentDto> upload(
        @RequestParam("file") MultipartFile file) {

        BinaryContentDto profileDto = binaryContentService.create(file);
        return ResponseEntity.status(HttpStatus.CREATED).body(profileDto);
    }
}

