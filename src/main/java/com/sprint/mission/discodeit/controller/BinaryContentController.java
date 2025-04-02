package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentFindResponse;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentUploadResponse;
import com.sprint.mission.discodeit.dto.common.ApiResponse;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/binaryContent")
@RequiredArgsConstructor
public class BinaryContentController {
    private final BinaryContentService binaryContentService;

    @PostMapping("/uploadSingle")
    public ResponseEntity<ApiResponse<BinaryContentUploadResponse>> uploadSingleFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    new ApiResponse<>(false, "파일이 선택되지 않았습니다.", null)
            );
        }
        BinaryContentUploadResponse response = binaryContentService.uploadSingle(file);
        ApiResponse<BinaryContentUploadResponse> apiResponse = new ApiResponse<>(true, "파일 업로드 성공", response);
        return ResponseEntity.status(HttpStatus.OK)
                .body(apiResponse);
    }

    @GetMapping("/find")
    public ResponseEntity<BinaryContentFindResponse> findById(@RequestParam("binaryContentId") UUID id) {
        BinaryContentFindResponse response = binaryContentService.findById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }
}
