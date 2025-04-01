package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {
    private final BinaryContentService binaryContentService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String contentType = file.getContentType();
        byte[] bytes = file.getBytes();

        BinaryContentCreateRequest request = new BinaryContentCreateRequest(fileName, contentType, bytes);
        BinaryContent savedBinaryContent = binaryContentService.create(request);

        return ResponseEntity.ok("파일이 업로드되었습니다. 파일 ID: " + savedBinaryContent.getId());
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<Resource> getFile(@PathVariable UUID fileId) {
        BinaryContent content = binaryContentService.find(fileId);

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(content.getContentType()))
                .body(new ByteArrayResource(content.getBytes()));
    }

    @GetMapping
    public ResponseEntity<String> getFiles() {
        List<BinaryContent> files = binaryContentService.findAllByIdIn(List.of());

        if (files.isEmpty()) {
            return ResponseEntity.status(404).body("저장된 파일이 없습니다.");
        }

        String fileNames = files.stream()
                .map(binaryContent -> binaryContent.getId().toString())
                .collect(Collectors.joining(", "));

        return ResponseEntity.ok("파일 목록: " + fileNames);
    }

    @GetMapping("/find")
    public ResponseEntity<Resource> getBinaryContent(@RequestParam UUID binaryContentId) {
        BinaryContent content = binaryContentService.find(binaryContentId);

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(content.getContentType()))
                .body(new ByteArrayResource(content.getBytes()));
    }
}
