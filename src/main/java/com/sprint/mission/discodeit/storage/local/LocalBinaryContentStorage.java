package com.sprint.mission.discodeit.storage.local;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.storage.FileStorageAsyncTaskExecutor;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
@Component
public class LocalBinaryContentStorage implements BinaryContentStorage {

  private final Path root;
  private final FileStorageAsyncTaskExecutor asyncTaskExecutor;

  public LocalBinaryContentStorage(
      @Value("${discodeit.storage.local.root-path}") Path root,
      FileStorageAsyncTaskExecutor asyncTaskExecutor
  ) {
    this.root = root;
    this.asyncTaskExecutor = asyncTaskExecutor;
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
    Supplier<UUID> putTask = () -> {
      Path filePath = resolvePath(binaryContentId);
      if (Files.exists(filePath)) {
        throw new IllegalArgumentException("File with key " + binaryContentId + " already exists");
      }
      try (OutputStream outputStream = Files.newOutputStream(filePath)) {
        outputStream.write(bytes);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      log.info("로컬에 binaryContent 업로드 성공: {}", binaryContentId);
      return binaryContentId;
    };

    CompletableFuture<UUID> putFuture = asyncTaskExecutor.executeAsync(putTask);

    try {
      // 3. 비동기 작업의 완료를 기다리고 결과 또는 예외를 받습니다.
      // .join() 사용 시 CompletionException 발생
      return putFuture.join();

    } catch (CompletionException e) { // 비동기 작업 중 발생한 예외
      Throwable actualCause = e.getCause(); // 실제 발생한 원본 예외를 얻습니다.

      log.error("바이너리 컨텐츠 저장 중 오류 발생. DB 롤백 예정: binaryContentId={}, cause={}",
          binaryContentId, actualCause.getMessage(), actualCause);

      // 4. DB 트랜잭션 롤백을 위해 예외를 다시 던짐
      // 스프링의 @Transactional은 RuntimeException이 발생해야 롤백을 수행
      if (actualCause instanceof IllegalArgumentException) {
        throw (IllegalArgumentException) actualCause;
      } else if (actualCause instanceof RuntimeException) {
        throw (RuntimeException) actualCause;
      } else {
        throw new RuntimeException("알 수 없는 비동기 저장 오류 발생", actualCause);
      }
    }
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
