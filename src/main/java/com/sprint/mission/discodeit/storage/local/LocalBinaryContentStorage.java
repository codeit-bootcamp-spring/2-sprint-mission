package com.sprint.mission.discodeit.storage.local;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
public class LocalBinaryContentStorage implements BinaryContentStorage {

  private final Path root;

  public LocalBinaryContentStorage(@Value("${discodeit.storage.local.root-path}") String rootPath) {
    this.root = Paths.get(rootPath);
  }

  @PostConstruct
  public void init() {
    try {
      if (Files.notExists(root)) {
        Files.createDirectories(root);
      }
    } catch (IOException e) {
      throw new RuntimeException("로컬 저장소 초기화 실패", e);
    }
  }

  public Path resolvePath(UUID id) {
    return root.resolve(id.toString());
  }

  @Override
  public UUID put(UUID id, byte[] bytes) {
    Path path = resolvePath(id);
    try {
      Files.write(path, bytes);
    } catch (IOException e) {
      throw new RuntimeException("Could not store file for ID: " + id, e);
    }
    return id;
  }

  @Override
  public InputStream get(UUID id) {
    Path path = resolvePath(id);
    try {
      if (Files.notExists(path)) {
        throw new NoSuchFileException("파일이 존재하지 않습니다: " + id);
      }
      return Files.newInputStream(path);
    } catch (IOException e) {
      throw new RuntimeException("Could not read file for ID: " + id, e);
    }
  }

  @Override
  public ResponseEntity<Resource> download(BinaryContentDto dto) {
    Path filePath = resolvePath(dto.id());
    log.debug("파일 경로: {}", filePath);
    try {
      InputStream inputStream = Files.newInputStream(filePath);
      String fileName = dto.fileName();
      log.debug("파일 이름: {}", fileName);

      InputStreamResource resource = new InputStreamResource(inputStream);
      log.debug("파일 리소스: {}", resource);

      return ResponseEntity.ok()
          .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
          .contentType(MediaType.APPLICATION_OCTET_STREAM)
          .body(resource);
    } catch (IOException e) {
      log.error("파일 다운로드 실패: {}", e.getMessage(), e);
      throw new RuntimeException("파일 다운로드 중 오류가 발생했습니다.", e);
    }
  }

  @Override
  public void deleteById(UUID id) {
    try {
      Files.deleteIfExists(resolvePath(id));
    } catch (IOException e) {
      throw new RuntimeException("파일 삭제 실패: " + id, e);
    }
  }
}
