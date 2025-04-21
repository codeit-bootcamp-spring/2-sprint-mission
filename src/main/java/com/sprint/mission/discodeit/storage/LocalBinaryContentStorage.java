package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
public class LocalBinaryContentStorage implements BinaryContentStorage {

  private Path root;

  public LocalBinaryContentStorage(@Value("${discodeit.storage.local.root-path}") String rootPath) {
    this.root = Paths.get(rootPath);
  }

  @PostConstruct
  public void init() {
    try {
      Files.createDirectories(root);
      System.out.println("로컬 저장소 초기화 완료: " + root.toAbsolutePath());
    } catch (IOException e) {
      throw new RuntimeException("로컬 저장소 초기화 실패: " + root, e);
    }
  }

  @Override
  public UUID put(UUID BinaryContentId, byte[] data) {
    Path path = resolvePath(BinaryContentId);
    try {
      Files.write(path, data);
      return BinaryContentId;
    } catch (IOException e) {
      throw new UncheckedIOException("파일 저장 실패 - id: " + BinaryContentId, e);
    }
  }

  @Override
  public InputStream get(UUID BinaryContentId) {
    Path path = resolvePath(BinaryContentId);
    try {
      return Files.newInputStream(path, StandardOpenOption.READ);
    } catch (IOException e) {
      throw new UncheckedIOException("파일 조회 실패 - id: " + BinaryContentId, e);
    }
  }

  @Override
  public ResponseEntity<?> download(BinaryContentDto content) {
    InputStream inputStream = get(content.id());
    Resource resource = new InputStreamResource(inputStream);
    return ResponseEntity.ok().body(resource);
  }

  private Path resolvePath(UUID id) {
    return root.resolve(id.toString());
  }
}
