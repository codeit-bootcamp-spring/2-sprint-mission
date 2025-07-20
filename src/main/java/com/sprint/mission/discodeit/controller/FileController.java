package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

    private final BinaryContentService binaryContentService;
    private final BinaryContentStorage binaryContentStorage;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String contentType = file.getContentType();
        byte[] bytes = file.getBytes();

        BinaryContentCreateRequest request = new BinaryContentCreateRequest(fileName, contentType, bytes);
        BinaryContentDto saved = binaryContentService.create(request);

        return ResponseEntity.ok("파일이 업로드되었습니다. 파일 ID: " + saved.id());
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<Resource> getFile(@PathVariable UUID fileId) throws IOException {
        BinaryContentDto content = binaryContentService.find(fileId);
        byte[] bytes = toBytes(binaryContentStorage.get(fileId));

        return ResponseEntity.ok()
            .contentType(MediaType.valueOf(content.contentType()))
            .body(new ByteArrayResource(bytes));
    }

    @GetMapping
    public ResponseEntity<String> getFiles() {
        List<BinaryContentDto> files = binaryContentService.findAllByIdIn(List.of());

        if (files.isEmpty()) {
            return ResponseEntity.status(404).body("저장된 파일이 없습니다.");
        }

        String fileNames = files.stream()
            .map(binaryContent -> binaryContent.id().toString())
            .collect(Collectors.joining(", "));

        return ResponseEntity.ok("파일 목록: " + fileNames);
    }

    @GetMapping("/find")
    public ResponseEntity<Resource> getBinaryContent(@RequestParam UUID binaryContentId) throws IOException {
        BinaryContentDto content = binaryContentService.find(binaryContentId);
        byte[] bytes = toBytes(binaryContentStorage.get(binaryContentId));

        return ResponseEntity.ok()
            .contentType(MediaType.valueOf(content.contentType()))
            .body(new ByteArrayResource(bytes));
    }

    private byte[] toBytes(InputStream inputStream) throws IOException {
        return inputStream.readAllBytes();
    }
}
