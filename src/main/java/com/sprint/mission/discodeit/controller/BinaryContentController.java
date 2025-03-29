package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.ResourceNotFoundException;
import com.sprint.mission.discodeit.jwt.RequiresAuth;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource; // InputStreamResource 사용
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional; // Optional 임포트
import java.util.UUID;


@RestController
@RequestMapping("/api/binary-contents") // 기본 경로 설정 (예시)
@RequiredArgsConstructor
public class BinaryContentController { // 클래스 이름 가정

    private final BinaryContentService binaryContentService; // 변경된 서비스 주입

    @RequiresAuth
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BinaryContentDto.Summary> uploadBinaryContent(

            @RequestParam("ownerId") UUID ownerId,
            @RequestParam("ownerType") String ownerType,
            @RequestPart("file") MultipartFile file) throws IOException { // IOException 처리 필요

        BinaryContentDto.Upload uploadDto = BinaryContentDto.Upload.builder()
                .ownerId(ownerId)
                .ownerType(ownerType)
                .file(file)
                .build();

        BinaryContentDto.Summary summary = binaryContentService.createBinaryContent(uploadDto);

        return ResponseEntity.ok(summary); // 200 OK
    }

    @RequiresAuth
    @GetMapping("/{contentId}")
    public ResponseEntity<Resource> downloadBinaryContent(@PathVariable UUID contentId) throws IOException {

        BinaryContent metadata = binaryContentService.getBinaryContentEntity(contentId);

        // 2. 실제 컨텐츠 스트림 얻기
        Optional<InputStream> streamOpt = binaryContentService.getContentStream(contentId);

        if (streamOpt.isEmpty()) {
            throw new ResourceNotFoundException("BinaryContent Stream", "id", contentId);
        }

        InputStreamResource resource = new InputStreamResource(streamOpt.get());

        String encodedFileName = UriUtils.encode(metadata.getFileName(), StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename*=UTF-8''" + encodedFileName;

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(metadata.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .contentLength(metadata.getSize())
                .body(resource);
    }


    @GetMapping("/summaries")
    public ResponseEntity<List<BinaryContentDto.Summary>> getBinaryContentSummaries(
            @RequestParam List<UUID> ids) {

        List<BinaryContentDto.Summary> summaries;
        summaries = binaryContentService.findBinaryContentSummariesByIds(ids);

        return ResponseEntity.ok(summaries);
    }


    @RequiresAuth
    @DeleteMapping
    public ResponseEntity<List<BinaryContentDto.DeleteResponse>> deleteBinaryContentsByIds(
            @RequestParam List<UUID> ids) {

        List<BinaryContentDto.DeleteResponse> results = binaryContentService.deleteBinaryContentsByIds(ids);
        return ResponseEntity.ok(results);
    }

    @RequiresAuth
    @DeleteMapping("/owner/{ownerId}")
    public ResponseEntity<Void> deleteBinaryContentsByOwner(@PathVariable UUID ownerId) {
        binaryContentService.deleteBinaryContentByOwner(ownerId);
        return ResponseEntity.noContent().build();
    }
}