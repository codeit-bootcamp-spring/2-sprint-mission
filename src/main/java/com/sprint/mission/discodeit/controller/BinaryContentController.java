package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.BinaryContentDownload;
import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.LocalBinaryContentStorage;
import com.sprint.mission.discodeit.util.LogMapUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/binaryContents")
@Tag(name = "BinaryContent", description = "첨부 파일 API")
public class BinaryContentController {

  private final BinaryContentService binaryContentService;
  private final LocalBinaryContentStorage localBinaryContentStorage;
  private static final Logger log = LoggerFactory.getLogger(BinaryContentController.class);

  @PostMapping
  public ResponseEntity<BinaryContentDto> create(@RequestBody BinaryContentCreateRequest request) {
    BinaryContentDto createdContent = binaryContentService.create(request);
    log.info("{}", LogMapUtil.of("action", "create")
        .add("request", request)
        .add("contentId", createdContent.id()));
    return new ResponseEntity<>(createdContent, HttpStatus.CREATED);
  }

  @GetMapping("/{binaryContentId}")
  public ResponseEntity<BinaryContentDto> read(@PathVariable UUID binaryContentId) {
    BinaryContentDto content = binaryContentService.findById(binaryContentId);
    log.info("{}", LogMapUtil.of("action", "read")
        .add("content", content));
    return ResponseEntity.ok(content);
  }

  @GetMapping
  public ResponseEntity<List<BinaryContentDto>> readAll(@RequestParam List<UUID> binaryContentIds) {
    List<BinaryContentDto> contents = binaryContentService.findAllByIds(binaryContentIds);
    log.info("{}", LogMapUtil.of("action", "readAll")
        .add("contents", contents));
    return ResponseEntity.ok(contents);
  }

  @GetMapping("/{binaryContentId}/download") // {binaryContentId}는 경로 변수로 사용됩니다.
  public ResponseEntity<Resource> downloadFile(@PathVariable UUID binaryContentId) {
    try {
      // 1. 서비스 레이어에게 다운로드에 필요한 정보 (데이터 스트림, 파일명, 타입)를 요청합니다.
      // 서비스는 Repository에서 메타데이터를, Storage에서 실제 데이터를 가져와 BinaryContentDownload DTO로 반환합니다.
      BinaryContentDownload downloadData = binaryContentService.download(binaryContentId);

      // 2. 서비스로부터 받은 정보를 바탕으로 HTTP 응답 헤더를 설정합니다.
      HttpHeaders headers = new HttpHeaders();
      // Content-Disposition 헤더 설정: 파일을 다운로드하도록 지정하고 파일명을 명시합니다.
      // 파일명에 특수 문자가 포함될 경우 인코딩 처리가 필요할 수 있습니다 (여기서는 단순화).
      headers.add(HttpHeaders.CONTENT_DISPOSITION,
          "attachment; filename=\"" + downloadData.filename() + "\"");
      // Content-Type 헤더 설정: 파일의 MIME 타입을 명시합니다.
      // headers.add(HttpHeaders.CONTENT_TYPE, downloadData.getContentType()); // 아래 contentType()으로 설정 가능

      // 3. ResponseEntity 객체를 구성하여 반환합니다.
      return ResponseEntity.ok() // HTTP 상태 코드 200 OK
          .headers(headers) // 설정한 헤더 추가
          // 서비스에서 제공된 컨텐츠 타입을 사용하여 응답의 Content-Type 헤더를 설정합니다.
          .contentType(MediaType.parseMediaType(downloadData.contentType()))
          // 서비스에서 제공된 InputStreamResource (파일 데이터)를 응답 본문에 설정합니다.
          .body(downloadData.getResource()); // BinaryContentDownload에서 제공하는 Resource 객체 사용

    } catch (IllegalArgumentException e) {
      // BinaryContent 메타데이터를 서비스에서 찾을 수 없는 경우 (서비스에서 throw)
      // logger.warn("Download failed: Metadata not found for id: " + binaryContentId, e); // 로깅 고려
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // HTTP 상태 코드 404 Not Found 응답
    } catch (IOException e) {
      // 스토리지에서 데이터 로딩 중 I/O 오류 발생 또는 데이터가 없는 경우 (서비스에서 throw)
      // 서비스에서 데이터 부재와 다른 I/O 오류를 구분하여 던진다면 여기서 더 세분화된 응답 가능 (예: 데이터 부재 시 404)
      // 현재는 모든 I/O 오류를 500으로 처리하거나, 데이터 부재도 포함하여 404로 처리할 수 있습니다.
      // logger.error("Download failed: Storage error for id: " + binaryContentId, e); // 로깅 고려
      // 여기서는 I/O 오류 전반을 Internal Server Error로 처리합니다.
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(null); // HTTP 상태 코드 500 Internal Server Error 응답
    } catch (Exception e) {
      // 그 외 예상치 못한 오류 발생 시
      // logger.error("Unexpected error during download for id: " + binaryContentId, e); // 로깅 고려
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(null); // HTTP 상태 코드 500 Internal Server Error 응답
    }
  }
}
