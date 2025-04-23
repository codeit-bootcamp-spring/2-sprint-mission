package com.sprint.discodeit.sprint.controller;

import com.sprint.discodeit.sprint.domain.dto.binaryContentDto.BinaryContentDto;
import com.sprint.discodeit.sprint.domain.storage.BinaryContentStorage;
import com.sprint.discodeit.sprint.service.basic.util.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/binaryContents")
@RequiredArgsConstructor
public class BinaryContentController {

    private final BinaryContentService binaryContentService;
    private final BinaryContentStorage binaryContentStorage;

    @GetMapping("/{binaryContentId}/download")
    public ResponseEntity<?> findBinaryContent(@PathVariable Long binaryContentId) {
        BinaryContentDto binaryContentDto = binaryContentService.find(binaryContentId);
        return binaryContentStorage.download(binaryContentDto);
    }
}
