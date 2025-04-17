package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import jakarta.annotation.PostConstruct;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
public class LocalBinaryContentStorage implements BinaryContentStorage {

  Path root;

  public LocalBinaryContentStorage(@Value("${discodeit.storage.local.root-path}") String rootPath) {
    this.root = Path.of(rootPath);
  }

  @PostConstruct
  public void init() {
    try {
      Files.createDirectories(root);
    } catch (IOException e) {
      throw new RuntimeException("Storage directory initialization failed", e);
    }
  }

  @Override
  public UUID put(UUID id, byte[] bytes) {
    try {
      Files.write(resolvePath(id), bytes);
      return id;
    } catch (IOException e) {
      throw new RuntimeException("File write failed", e);
    }
  }

  @Override
  public InputStream get(UUID id) {
    try {
      return new BufferedInputStream(Files.newInputStream(resolvePath(id)));
    } catch (IOException e) {
      throw new RuntimeException("File read failed", e);
    }
  }

  @Override
  public ResponseEntity<Resource> download(BinaryContentDto binaryContentDto) {
    try (InputStream in = get(binaryContentDto.id())) {
      ByteArrayResource resource = new ByteArrayResource(in.readAllBytes());

      return ResponseEntity.ok()
          .contentType(MediaType.parseMediaType(binaryContentDto.contentType()))
          .header(HttpHeaders.CONTENT_DISPOSITION,
              "attachment; filename=\"" + binaryContentDto.fileName() + "\"")
          .body(resource);
    } catch (IOException e) {
      throw new RuntimeException("Download failed", e);
    }
  }

  private Path resolvePath(UUID id) {
    return root.resolve(id.toString());
  }
}
