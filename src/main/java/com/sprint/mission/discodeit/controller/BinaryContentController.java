package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ApiResponse;
import com.sprint.mission.discodeit.dto.FindBinaryContentRequestDto;
import com.sprint.mission.discodeit.dto.SaveBinaryContentParamDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/binaryContent")
@RequiredArgsConstructor
public class BinaryContentController {

    private final BinaryContentService binaryContentService;

    @PostMapping("/save")
    public ResponseEntity<ApiResponse<Void>> save(@RequestParam("file") MultipartFile file) throws IOException {
        binaryContentService.save(SaveBinaryContentParamDto.from(file));
        return ResponseEntity.ok(ApiResponse.success());
    }

    @GetMapping("/find")
    public ResponseEntity<ApiResponse<FindBinaryContentRequestDto>> find(@RequestParam UUID binaryContentId) {
        binaryContentService.findById(binaryContentId);
        return ResponseEntity.ok(ApiResponse.success(binaryContentService.findById(binaryContentId)));
    }
}
