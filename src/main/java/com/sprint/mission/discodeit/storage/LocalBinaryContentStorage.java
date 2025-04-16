package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
public class LocalBinaryContentStorage implements BinaryContentStorage {

  private final Path root;

  public LocalBinaryContentStorage(@Value("${discodeit.storage.local.root-path}") String rootPath) {
    this.root = Path.of(rootPath);
  }

  @PostConstruct
  public void init() {
    try {
      Files.createDirectories(root);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private Path resolvePath(UUID id) {
    return root.resolve(id.toString());
  }

  @Override
  public UUID put(UUID binaryContentId, byte[] binaryContent) {
    try {
      Files.write(resolvePath(binaryContentId), binaryContent, StandardOpenOption.CREATE);
      return binaryContentId;
    } catch (IOException e) {
      throw new RuntimeException("파일 저장 실패: " + e.getMessage());
    }
  }

  @Override
  public InputStream get(UUID binaryContentId) {
    try {
      return Files.newInputStream(resolvePath(binaryContentId), StandardOpenOption.READ);
    } catch (IOException e) {
      throw new RuntimeException("파일 읽기 실패: " + e.getMessage());
    }
  }

  @Override
  public ResponseEntity<?> download(BinaryContentDto binaryContentDto) {
    try {
      byte[] bytes = Files.readAllBytes(resolvePath(binaryContentDto.id()));
      ByteArrayResource resource = new ByteArrayResource(bytes);

      return ResponseEntity.ok()
          .header(HttpHeaders.CONTENT_DISPOSITION,
              "attachment; filename=\"" + binaryContentDto.fileName() + "\"")
          .contentType(MediaType.parseMediaType(binaryContentDto.contentType()))
          .contentLength(binaryContentDto.size())
          .body(resource);
    } catch (IOException e) {
      throw new RuntimeException("파일 다운로드 실패: " + e.getMessage());
    }
  }
}
