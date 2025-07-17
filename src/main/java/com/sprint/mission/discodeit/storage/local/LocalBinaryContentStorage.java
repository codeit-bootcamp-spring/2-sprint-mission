package com.sprint.mission.discodeit.storage.local;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.entity.AsyncTaskFailure;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.NoSuchElementException;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
@Component
public class LocalBinaryContentStorage implements BinaryContentStorage {

  private final Path root;

  public LocalBinaryContentStorage(
      @Value("${discodeit.storage.local.root-path}") Path root
  ) {
    this.root = root;
  }

  @PostConstruct
  public void init() {
    if (!Files.exists(root)) {
      try {
        Files.createDirectories(root);
      } catch (IOException e) {
        e.printStackTrace();
        throw new RuntimeException(e);
      }
    }
  }

  public UUID put(UUID binaryContentId, byte[] bytes) {
    log.info("로컬 파일 저장 요청: {} ({}bytes)", binaryContentId, bytes.length);

    saveFileAsync(binaryContentId, bytes);

    return binaryContentId;
  }

  @Async("storageTaskExecutor")
  @Retryable(
          retryFor = {IOException.class, RuntimeException.class},
          maxAttempts = 3,
          backoff = @Backoff(delay = 1000, multiplier = 2.0)
  )
  void saveFileAsync(UUID binaryContentId, byte[] bytes) {
    try {
      Path filePath = resolvePath(binaryContentId);

      if (Files.exists(filePath)) {
        log.warn("파일이 이미 존재합니다: {}", binaryContentId);
        return;
      }

      log.info("로컬 파일 저장 시작: {}", binaryContentId);

      try (OutputStream outputStream = Files.newOutputStream(filePath)) {
        outputStream.write(bytes);
        log.info("로컬 파일 저장 성공: {}", binaryContentId);
      }

    } catch (IOException e) {
      log.error("로컬 파일 저장 실패: {}", binaryContentId, e);
    }
  }

  @Recover
  public void recoverSaveFileAsync(RuntimeException e, UUID binaryContentId, byte[] bytes) {
    String requestId = MDC.get("requestId");
    AsyncTaskFailure failure = new AsyncTaskFailure("saveFileAsyncLocal" , requestId , e.getMessage());
    log.error("비동기 로컬 파일 저장 복구 로직 실행: {}", failure, e);
  }


  public InputStream get(UUID binaryContentId) {
    Path filePath = resolvePath(binaryContentId);
    if (Files.notExists(filePath)) {
      throw new NoSuchElementException("File with key " + binaryContentId + " does not exist");
    }
    try {
      return Files.newInputStream(filePath);
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  private Path resolvePath(UUID key) {
    return root.resolve(key.toString());
  }

  @Override
  public ResponseEntity<Resource> download(BinaryContentDto metaData) {
    InputStream inputStream = get(metaData.id());
    Resource resource = new InputStreamResource(inputStream);

    return ResponseEntity
        .status(HttpStatus.OK)
        .header(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + metaData.fileName() + "\"")
        .header(HttpHeaders.CONTENT_TYPE, metaData.contentType())
        .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(metaData.size()))
        .body(resource);
  }
}
