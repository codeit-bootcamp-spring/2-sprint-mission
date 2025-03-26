package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentUploadResponse;
import com.sprint.mission.discodeit.dto.common.ApiResponse;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/attachments")
@RequiredArgsConstructor
public class BinaryContentController {
    private final BinaryContentService binaryContentService;
    private final FileStorageService fileStorageService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<BinaryContentUploadResponse>> upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    new ApiResponse<>(false, "파일이 선택되지 않았습니다.", null)
            );
        }
        // 파일 저장
        UUID fileId = UUID.randomUUID();
        BinaryContentCreateRequest request = fileStorageService.uploadFile(file, fileId);
        // 메타데이터 저장
        binaryContentService.create(request);
        // 응답 생성
        BinaryContentUploadResponse response = new BinaryContentUploadResponse(request.fileId(), request.filePath(), request.fileName(), request.fileType(), request.fileSize());
        ApiResponse<BinaryContentUploadResponse> apiResponse = new ApiResponse<>(true, "파일 업로드 성공", response);
        return ResponseEntity.ok(apiResponse);
    }
}
