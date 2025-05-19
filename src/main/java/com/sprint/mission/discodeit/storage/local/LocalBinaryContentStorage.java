package com.sprint.mission.discodeit.storage.local;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.binaryContent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.exception.binaryContent.DuplicateBinaryContentException;
import com.sprint.mission.discodeit.exception.storage.CreateDirectoryException;
import com.sprint.mission.discodeit.exception.storage.FileReadException;
import com.sprint.mission.discodeit.exception.storage.FileWriteException;
import com.sprint.mission.discodeit.exception.storage.InputStreamException;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        log.warn("이미 디렉토리 생성 실패. root: {}", root.toString());
        Map<String, Object> details = new HashMap<>();
        details.put("root", root);
        throw new CreateDirectoryException(Instant.now(), ErrorCode.CANT_CRATE_DIRECTORY, details);
      }
    }
  }

  public UUID put(UUID binaryContentId, byte[] bytes) {
    Path filePath = resolvePath(binaryContentId);
    if (Files.exists(filePath)) {
      log.warn("이미 존재하는 파일 ID 생성을 시도했습니다. filePath: {}", filePath);
      Map<String, Object> details = new HashMap<>();
      details.put("filePath", filePath);
      throw new DuplicateBinaryContentException(Instant.now(), ErrorCode.DUPLICATE_BINARYCONTENT_ID, details);
    }
    try (OutputStream outputStream = Files.newOutputStream(filePath)) {
      outputStream.write(bytes);
    } catch (IOException e) {
      log.warn("파일 쓰기에 실패했습니다. filePath: {}", filePath);
      Map<String, Object> details = new HashMap<>();
      details.put("filePath", filePath);
      throw new FileWriteException(Instant.now(), ErrorCode.CANT_FILE_WRITE, details);
    }
    return binaryContentId;
  }

  public InputStream get(UUID binaryContentId) {
    Path filePath = resolvePath(binaryContentId);
    if (Files.notExists(filePath)) {
      log.warn("해당 경로에 파일이 존재하지 않습니다. filePath: {}", filePath);
      Map<String, Object> details = new HashMap<>();
      details.put("filePath", filePath);
      throw new BinaryContentNotFoundException(Instant.now(), ErrorCode.BINARYCONTENT_NOT_FOUND, details);
    }
    try {
      return Files.newInputStream(filePath);
    } catch (IOException e) {
      log.warn("파일 읽기 실패. filePath: {}", filePath);
      Map<String, Object> details = new HashMap<>();
      details.put("filePath", filePath);
      throw new FileReadException(Instant.now(), ErrorCode.CANT_FILE_READ, details);
    }
  }

  private Path resolvePath(UUID key) {
    return root.resolve(key.toString());
  }

  @Override
  public ResponseEntity<Resource> download(BinaryContentDto metaData) {
    try (InputStream inputStream = get(metaData.id())) {
      Resource resource = new InputStreamResource(inputStream);

      return ResponseEntity
          .status(HttpStatus.OK)
          .header(HttpHeaders.CONTENT_DISPOSITION,
              "attachment; filename=\"" + metaData.fileName() + "\"")
          .header(HttpHeaders.CONTENT_TYPE, metaData.contentType())
          .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(metaData.size()))
          .body(resource);
    } catch (IOException e) {
      log.warn("inputStream 열기 실패. metaDataId: {}", metaData.id());
      Map<String, Object> details = new HashMap<>();
      details.put("metaDataId", metaData.id());
      throw new InputStreamException(Instant.now(), ErrorCode.INPUTSTREAM_FAILED, details);
    }
  }

}
