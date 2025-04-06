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
@RequestMapping("/api/binaryContents")
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
    public ResponseEntity<List<BinaryContentResult>> getByIdIn(@RequestParam(value = "binaryContentIds") List<UUID> binaryContentIds) {
        List<BinaryContentResult> binaryContentResults = binaryContentService.getByIdIn(binaryContentIds);

        return ResponseEntity.ok(binaryContentResults);
    }

    @GetMapping("/{binaryContentId}")
    public ResponseEntity<BinaryContentResult> getById(@PathVariable UUID binaryContentId) {
        BinaryContentResult binaryContentResult = binaryContentService.getById(binaryContentId);

        return ResponseEntity.ok()
                .body(binaryContentResult);
    }

    @DeleteMapping("/{binaryContentId}")
    public ResponseEntity<Void> delete(@PathVariable UUID binaryContentId) {
        binaryContentService.delete(binaryContentId);

        return ResponseEntity.noContent().build();
    }
}
