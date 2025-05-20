package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentResponse;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
public class LocalBinaryContentStorage implements BinaryContentStorage {

  private final Path root;

  public LocalBinaryContentStorage(
      @Value("${discodeit.storage.local.root-path}") String defaultPath) {
    this.root = Paths.get(defaultPath);
  }

  @PostConstruct
  public void init() {
    try {
      Files.createDirectories(root);
    } catch (IOException e) {
      throw new RuntimeException("로컬 저장 디렉토리 초기화 실패: " + root, e);
    }
  }

  @Override
  public UUID put(UUID binaryContentId, byte[] bytes) {
    try {
      Files.write(resolvePath(binaryContentId), bytes);
      return binaryContentId;
    } catch (IOException e) {
      throw new RuntimeException("파일 저장 실패: " + root, e);
    }
  }

  @Override
  public InputStream get(UUID binaryContentId) {
    try {
      return Files.newInputStream(resolvePath(binaryContentId));
    } catch (IOException e) {
      throw new RuntimeException("파일 로딩 실패: " + root, e);
    }
  }

  @Override
  public ResponseEntity<?> download(BinaryContentResponse binaryContentResponse) {
    try {
      Path path = resolvePath(binaryContentResponse.id());
      InputStream in = Files.newInputStream(path);
      Resource resource = new InputStreamResource(in);

      return ResponseEntity.ok()
          .header(HttpHeaders.CONTENT_DISPOSITION,
              "attachment; filename=\"" + binaryContentResponse.id() + "\"")
          .contentType(MediaType.parseMediaType(binaryContentResponse.contentType()))
          .contentLength(Files.size(path))
          .body(resource);

    } catch (IOException e) {
      return ResponseEntity.internalServerError().build();
    }
  }

  @Override
  public void delete(UUID binaryContentId) {
    try {
      Files.deleteIfExists(resolvePath(binaryContentId));
    } catch (IOException e) {
      throw new RuntimeException("파일 삭제 실패: " + binaryContentId, e);
    }
  }

  private Path resolvePath(UUID binaryContentId) {
    return root.resolve(binaryContentId.toString());
  }


}
