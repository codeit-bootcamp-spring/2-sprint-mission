package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ApiResponse;
import com.sprint.mission.discodeit.dto.FindBinaryContentRequestDto;
import com.sprint.mission.discodeit.dto.SaveBinaryContentRequestDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/binaryContent")
@RequiredArgsConstructor
public class BinaryContentController {

    private final BinaryContentService binaryContentService;

    @GetMapping("/{binaryContentId}")
    public ResponseEntity<ApiResponse<FindBinaryContentRequestDto>> find(
            @PathVariable UUID binaryContentId
    ) {
        binaryContentService.findById(binaryContentId);
        return ResponseEntity.ok(ApiResponse.success(binaryContentService.findById(binaryContentId)));
    }

    @GetMapping("/find")
    public ResponseEntity<ApiResponse<List<FindBinaryContentRequestDto>>> findByIdIn(
            @RequestParam List<UUID> binaryContentIdList
    ) {
        List<FindBinaryContentRequestDto> result = binaryContentService.findByIdIn(binaryContentIdList);
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}
