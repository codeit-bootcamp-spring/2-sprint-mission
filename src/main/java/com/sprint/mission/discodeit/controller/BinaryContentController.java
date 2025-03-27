package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.common.ApiResponse;
import com.sprint.mission.discodeit.entity.common.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/binaryContents")
@RequiredArgsConstructor
public class BinaryContentController {

    private final BinaryContentService binaryContentService;

    @GetMapping("/{binaryContentId}")
    public ResponseEntity<ApiResponse<BinaryContent>> find(@PathVariable UUID binaryContentId) {
        BinaryContent content = binaryContentService.find(binaryContentId);
        ApiResponse<BinaryContent> response = new ApiResponse<>("바이너리 파일 조회 성공", content);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BinaryContent>>> findAll(@RequestParam List<UUID> ids) {
        List<BinaryContent> contents = binaryContentService.findAllByIdIn(ids);
        ApiResponse<List<BinaryContent>> response = new ApiResponse<>("바이너리 파일 목록 조회 성공", contents);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<BinaryContent>> upload(@RequestParam("file")MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("파일이 없습니다", null));
        }

        BinaryContentCreateRequest request = BinaryContentCreateRequest.fromMultipartFile(file);
        BinaryContent content = binaryContentService.create(request);
        ApiResponse<BinaryContent> response = new ApiResponse<>("바이너리 파일 생성 성공", content);
        return ResponseEntity.ok(response);
    }

}
