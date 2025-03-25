package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/binaryContent")
public class BinaryContentController {

    private final BinaryContentService binaryContentService;

    public BinaryContentController(BinaryContentService binaryContentService) {
        this.binaryContentService = binaryContentService;
    }

    // ID 기반 이미지 조회
    @GetMapping("/find")
    public ResponseEntity<BinaryContent> getImage(@PathVariable UUID imageId) {
        BinaryContent binaryContent = binaryContentService.find(imageId);
        buildImageResponse(binaryContent);
        return ResponseEntity.ok(binaryContent);
    }

    // 파일명 기반 이미지 조회
    @GetMapping("/filename/{filename}")
    public ResponseEntity<Resource> getImageByFilename(@PathVariable String filename) {
        BinaryContent binaryContent = binaryContentService.findByFilename(filename);
        return buildImageResponse(binaryContent);
    }

    // 공통 응답 빌더
    private ResponseEntity<Resource> buildImageResponse(BinaryContent binaryContent) {
        ByteArrayResource resource = new ByteArrayResource(binaryContent.getBytes());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, binaryContent.getContentType())
                .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(binaryContent.getSize()))
                .body(resource);
    }
}
