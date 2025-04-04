package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("/binary-content")
public class BinaryContentController {

    private final BinaryContentRepository binaryContentRepository;

    public BinaryContentController(BinaryContentRepository binaryContentRepository) {
        this.binaryContentRepository = binaryContentRepository;
    }

    // 단일
    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getBinaryContent(@PathVariable UUID id) {
        BinaryContent content = binaryContentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Binary content with id " + id + " not found"));

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(content.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + content.getFileName() + "\"")
                .body(content.getBytes());
    }

    // 다회
    @GetMapping
    public List<BinaryContentDto> getBinaryContents(@RequestParam List<UUID> ids) {
        return binaryContentRepository.findAllByIdIn(ids).stream()
                .map(BinaryContentDto::from)
                .toList();
    }
}
