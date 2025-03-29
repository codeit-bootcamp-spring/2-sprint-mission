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
import java.util.UUID;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {
    private final BinaryContentService binaryContentService;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            String contentType = file.getContentType();
            byte[] bytes = file.getBytes();

            BinaryContentCreateRequest request = new BinaryContentCreateRequest(fileName, contentType, bytes);
            BinaryContent savedBinaryContent = binaryContentService.create(request);

            return ResponseEntity.ok("파일이 업로드되었습니다. 파일 ID: " + savedBinaryContent.getId());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("파일 업로드 중 오류가 발생했습니다.");
        }
    }

    @RequestMapping(value = "/{fileId}", method = RequestMethod.GET)
    public ResponseEntity<Resource> getFile(@PathVariable UUID fileId) {
        BinaryContent content = binaryContentService.find(fileId);

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(content.getContentType()))
                .body(new ByteArrayResource(content.getBytes()));
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> getFiles() {
        return ResponseEntity.ok("여러 파일이 다운로드되었습니다.");
    }

    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public ResponseEntity<Resource> getBinaryContent(@RequestParam UUID binaryContentId) {
        BinaryContent content = binaryContentService.find(binaryContentId);

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(content.getContentType()))
                .body(new ByteArrayResource(content.getBytes()));
    }
}