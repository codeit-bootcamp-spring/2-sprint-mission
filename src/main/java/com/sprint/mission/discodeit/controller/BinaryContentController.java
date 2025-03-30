package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.ResourceNotFoundException;
import com.sprint.mission.discodeit.jwt.RequiresAuth;
import com.sprint.mission.discodeit.service.BinaryContentService;
import jakarta.servlet.http.HttpServletResponse;
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
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


@RestController
@RequestMapping("/binary-contents")
@RequiredArgsConstructor
public class BinaryContentController {

    private final BinaryContentService binaryContentService;

    @RequiresAuth
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BinaryContentDto.Summary> uploadBinaryContent(

            @RequestParam("ownerId") UUID ownerId,
            @RequestParam("ownerType") String ownerType,
            @RequestPart("file") MultipartFile file) throws IOException {

        BinaryContentDto.Upload uploadDto = BinaryContentDto.Upload.builder()
                .ownerId(ownerId)
                .ownerType(ownerType)
                .file(file)
                .build();

        BinaryContentDto.Summary summary = binaryContentService.createBinaryContent(uploadDto);

        return ResponseEntity.ok(summary);
    }

    @RequiresAuth
    @GetMapping("/{contentId}")
    public ResponseEntity<Resource> downloadBinaryContent(@PathVariable UUID contentId) throws IOException {

        BinaryContent metadata = binaryContentService.getBinaryContentEntity(contentId);

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
    @RequiresAuth
    @GetMapping("/download-archive")
    public void downloadFilesAsZip(
            @RequestParam List<UUID> ids,
            HttpServletResponse response) throws IOException {

        String zipFileName = "download_" + System.currentTimeMillis() + ".zip"; // 동적 파일명 생성
        String encodedZipFileName = UriUtils.encode(zipFileName, StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename*=UTF-8''" + encodedZipFileName;

        response.setContentType("application/zip");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, contentDisposition);

        try (ZipOutputStream zos = new ZipOutputStream(response.getOutputStream())) {

            for (UUID contentId : ids) {

                BinaryContent metadata = binaryContentService.getBinaryContentEntity(contentId);
                Optional<InputStream> streamOpt = binaryContentService.getContentStream(contentId);

                if (streamOpt.isPresent() && metadata != null) {
                    try (InputStream fileStream = streamOpt.get()) {

                        String entryName = metadata.getId() + "_" + metadata.getFileName();
                        ZipEntry zipEntry = new ZipEntry(entryName);
                        zos.putNextEntry(zipEntry);
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = fileStream.read(buffer)) > 0) {
                            zos.write(buffer, 0, len);
                        }
                        zos.closeEntry();
                    } catch (IOException e) {

                        System.err.println("Error processing file for zipping: " + contentId + ", Error: " + e.getMessage());
                    }
                }
                }
            zos.finish();
        }
    }

    @GetMapping("/summaries")
    public ResponseEntity<List<BinaryContentDto.Summary>> downloadBinaryContents(
            @RequestParam List<UUID> ids) {

        List<BinaryContentDto.Summary> summaries;
        summaries = binaryContentService.findBinaryContentSummariesByIds(ids);

        return ResponseEntity.ok(summaries);
    }


    @RequiresAuth
    @DeleteMapping("/delete/{ids}")
    public ResponseEntity<List<BinaryContentDto.DeleteResponse>> deleteBinaryContentsByIds(
            @RequestParam List<UUID> ids) {

        List<BinaryContentDto.DeleteResponse> results = binaryContentService.deleteBinaryContentsByIds(ids);
        return ResponseEntity.ok(results);
    }

    @RequiresAuth
    @DeleteMapping("/delete/{ownerId}")
    public ResponseEntity<Void> deleteBinaryContentsByOwner(@PathVariable UUID ownerId) {
        binaryContentService.deleteBinaryContentByOwner(ownerId);
        return ResponseEntity.noContent().build();
    }
}