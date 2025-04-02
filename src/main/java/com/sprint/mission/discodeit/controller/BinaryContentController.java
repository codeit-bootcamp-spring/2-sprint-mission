package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.jwt.RequiresAuth;
import com.sprint.mission.discodeit.service.BinaryContentService;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;


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
  public ResponseEntity<Resource> downloadBinaryContent(@PathVariable UUID contentId)
      throws IOException {

    BinaryContent metadata = binaryContentService.getBinaryContentEntity(contentId);
    Optional<InputStream> streamOpt = binaryContentService.getContentStream(contentId);
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

    String zipFileName = "download_" + System.currentTimeMillis() + ".zip";
    String encodedZipFileName = UriUtils.encode(zipFileName, StandardCharsets.UTF_8);
    String contentDisposition = "attachment; filename*=UTF-8''" + encodedZipFileName;

    response.setContentType("application/zip");
    response.setHeader(HttpHeaders.CONTENT_DISPOSITION, contentDisposition);

    binaryContentService.writeFilesAsZip(ids, response.getOutputStream());
  }


  @GetMapping("/summaries")
  public ResponseEntity<List<BinaryContentDto.Summary>> downloadBinaryContents(
      @RequestParam List<UUID> ids) {

    List<BinaryContentDto.Summary> summaries;
    summaries = binaryContentService.findBinaryContentSummariesByIds(ids);

    return ResponseEntity.ok(summaries);
  }


  @RequiresAuth
  @DeleteMapping()
  public ResponseEntity<List<BinaryContentDto.DeleteResponse>> deleteBinaryContentsByIds(
      @RequestParam List<UUID> ids) {

    List<BinaryContentDto.DeleteResponse> results = binaryContentService.deleteBinaryContentsByIds(
        ids);
    return ResponseEntity.ok(results);
  }

  @RequiresAuth
  @DeleteMapping("/delete/{ownerId}")
  public ResponseEntity<Void> deleteBinaryContentsByOwner(@PathVariable UUID ownerId) {
    binaryContentService.deleteBinaryContentByOwner(ownerId);
    return ResponseEntity.noContent().build();
  }
}