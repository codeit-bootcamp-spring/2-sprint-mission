package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BinaryContentController {

    private final BinaryContentService binaryContentService;

    private final BinaryContentStorage binaryContentStorage;

    @GetMapping("/api/binaryContents")
    public ResponseEntity<List<BinaryContentDto>> getBinaryContents(
        @RequestParam List<UUID> binaryContentIds
    ) {

        List<BinaryContentDto> response = binaryContentService.findAllByIdIn(binaryContentIds);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/binaryContents/{binaryContentId}")
    public ResponseEntity<BinaryContentDto> getBinaryContent(
        @PathVariable UUID binaryContentId) {

        BinaryContentDto response = binaryContentService.findById(binaryContentId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/binaryContents/{binaryContentId}/download")
    public ResponseEntity<?> downloadBinaryContent(
        @PathVariable UUID binaryContentId) {

        BinaryContentDto binaryContentDto = binaryContentService.findById(binaryContentId);
        return binaryContentStorage.download(binaryContentDto);
    }
}
