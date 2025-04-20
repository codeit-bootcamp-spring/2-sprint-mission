package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.BinaryContentApi;
import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.BinaryContentStorage;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/binaryContents")
public class BinaryContentController implements BinaryContentApi {

    private final BinaryContentService binaryContentService;
    private final BinaryContentStorage binaryContentStorage;
    private final BinaryContentMapper binaryContentMapper;

    @GetMapping(path = "{binaryContentId}")
    public ResponseEntity<BinaryContent> find(
        @PathVariable("binaryContentId") UUID binaryContentId) {
        BinaryContent binaryContent = binaryContentService.find(binaryContentId);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(binaryContent);
    }

    @GetMapping
    public ResponseEntity<List<BinaryContent>> findAllByIdIn(
        @RequestParam("binaryContentIds") List<UUID> binaryContentIds) {
        List<BinaryContent> binaryContents = binaryContentService.findAllByIdIn(binaryContentIds);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(binaryContents);
    }

    @GetMapping("/{binaryContentId}/download")
    public ResponseEntity<?> download(
        @PathVariable("binaryContentId") UUID binaryContentId) {

        BinaryContent binaryContent = binaryContentService.find(binaryContentId);
        BinaryContentDto dto = binaryContentMapper.toDto(binaryContent);
        return binaryContentStorage.download(dto);
    }

    @PostMapping("/upload")
    public ResponseEntity<BinaryContentDto> upload(
        @RequestParam("file") MultipartFile file) throws IOException {

        if (file != null && !file.isEmpty()) {
            BinaryContentCreateRequest createRequest = new BinaryContentCreateRequest(
                file.getOriginalFilename(),
                file.getContentType(),
                file.getBytes()
            );
            BinaryContentDto binaryContentDto = binaryContentService.create(createRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(binaryContentDto);
        }
        return ResponseEntity.badRequest().build();
    }
}
