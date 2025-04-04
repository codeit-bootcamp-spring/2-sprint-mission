package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.application.dto.binarycontent.BinaryContentResult;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/binary-contents")
@RequiredArgsConstructor
public class BinaryContentController {

    private final BinaryContentService binaryContentService;

    @PostMapping
    public ResponseEntity<BinaryContentResult> createProfileImage(@RequestPart MultipartFile multipartFile) {
        BinaryContentResult binaryContentResult = binaryContentService.createProfileImage(
                multipartFile);

        return ResponseEntity.ok(binaryContentResult);
    }

    @GetMapping
    public ResponseEntity<List<BinaryContentResult>> getByIdIn(@RequestParam(value = "ids") List<UUID> ids) {
        List<BinaryContentResult> binaryContentResults = binaryContentService.getByIdIn(ids);

        return ResponseEntity.ok(binaryContentResults);
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<BinaryContentResult> getById(@PathVariable UUID fileId) {
        BinaryContentResult binaryContentResult = binaryContentService.getById(fileId);

        return ResponseEntity.ok()
                .body(binaryContentResult);
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<Void> delete(@PathVariable UUID fileId) {
        binaryContentService.delete(fileId);

        return ResponseEntity.noContent().build();
    }
}
