package com.sprint.mission.discodeit.storage.local;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
public class LocalBinaryContentStorage implements BinaryContentStorage {

  private final FileSystem fileSystem = FileSystems.getDefault();

  @Value("${discodeit.storage.local.root-path}")
  private String rootPathStr;

  private Path root;

  @PostConstruct
  public void init() {
    this.root = Paths.get(rootPathStr);
    try {
      if (Files.notExists(root)) {
        Files.createDirectories(root);
        log.info("Created binary storage directory at: {}", root.toAbsolutePath());
      }
    } catch (IOException e) {
      throw new RuntimeException("Failed to initialize binary storage directory", e);
    }
  }

  private Path resolvePath(UUID id) {
    return root.resolve(id.toString());
  }

  @Override
  public UUID put(UUID id, byte[] data) {
    Path path = resolvePath(id);
    try {
      Files.write(path, data, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
      return id;
    } catch (IOException e) {
      throw new RuntimeException("Failed to store file: " + id, e);
    }
  }

  @Override
  public InputStream get(UUID id) {
    Path path = resolvePath(id);
    try {
      return Files.newInputStream(path, StandardOpenOption.READ);
    } catch (IOException e) {
      throw new RuntimeException("File not found: " + id, e);
    }
  }

  @Override
  public ResponseEntity<Resource> download(BinaryContentDto dto) {
    try {
      InputStream is = get(dto.id());
      InputStreamResource resource = new InputStreamResource(is);

      return ResponseEntity.ok()
          .header(HttpHeaders.CONTENT_DISPOSITION,
              "attachment; filename=\"" + dto.fileName() + "\"")
          .contentType(MediaType.parseMediaType(dto.contentType()))
          .contentLength(dto.size())
          .body(resource);
    } catch (Exception e) {
      log.error("Failed to download file: {}", dto.id(), e);
      return ResponseEntity.notFound().build();
    }
  }
}
