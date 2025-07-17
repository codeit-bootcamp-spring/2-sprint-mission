package com.sprint.mission.discodeit.storage.local;

import com.sprint.mission.discodeit.dto.data.AsyncTaskFailure;
import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
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

  //  @Async
  @Retryable(
      retryFor = {RuntimeException.class}, // 재시도할 예외 유형
      maxAttempts = 3, // 최대 재시도 횟수 (최초 시도 포함, 총 3회)
      backoff = @Backoff(delay = 1000, maxDelay = 5000) // 대기 시간 ( 초기 1초, 최대 5초 )
  )
  public UUID put(UUID binaryContentId, byte[] bytes) {
    Path filePath = resolvePath(binaryContentId);
    if (Files.exists(filePath)) {
      throw new IllegalArgumentException("File with key " + binaryContentId + " already exists");
    }
    try {
      Thread.sleep(2000);
      try (OutputStream outputStream = Files.newOutputStream(filePath)) {
        outputStream.write(bytes);
      }
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
    return binaryContentId;
  }

  @Recover
  public UUID recoverPut(RuntimeException e, UUID binaryContentId, byte[] bytes) {
    Throwable cause = e.getCause();
    String failureReason = (cause instanceof IOException) ? cause.getMessage() : e.getMessage();
    String requestId = MDC.get("requestId");

    AsyncTaskFailure failure = new AsyncTaskFailure("Local putMethod", requestId, failureReason);

    log.error("업로드 실패: {}, 컨텐츠 ID: {}", failure, binaryContentId);

    return null;
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
