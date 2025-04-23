package com.sprint.mission.discodeit.storage.local;

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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
public class LocalBinaryContentStorage implements BinaryContentStorage {

  private final Path root;

  public LocalBinaryContentStorage(
      @Value("${discodeit.storage.local.root-path}") Path root) {
    this.root = root;
  }

  @PostConstruct
  public void init() throws IOException {
    if (!Files.exists(root)) {
      Files.createDirectories(root);
    }
  }

  private Path resolvePath(UUID id) {
    return root.resolve(id.toString());
  }

  @Override
  public UUID put(UUID binaryContentId, byte[] bytes) {
    Path filePath = resolvePath(binaryContentId);

    if (Files.exists(filePath)) {
      throw new IllegalArgumentException("File with key " + binaryContentId + " already exists.");
    }

    try (OutputStream outputStream = Files.newOutputStream(filePath)) {
      outputStream.write(bytes);
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException("Failed to save file: " + filePath, e);
    }

    return binaryContentId;
  }

  @Override
  public InputStream get(UUID binaryContentId) {
    Path filePath = resolvePath(binaryContentId);

    try {
      return Files.newInputStream(filePath);
    } catch (IOException e) {
      e.printStackTrace();
      throw new NoSuchElementException("File with key " + binaryContentId + " not found.");
    }
  }

  @Override
  public ResponseEntity<?> download(BinaryContentDto binaryContent) {
    try {
      InputStream inputStream = get(binaryContent.id());
      Resource resource = new InputStreamResource(inputStream);

      return ResponseEntity.ok()
          .header(HttpHeaders.CONTENT_DISPOSITION,
              "attachment; filename=\"" + binaryContent.fileName() + "\"")
          .header(HttpHeaders.CONTENT_TYPE, binaryContent.contentType())
          .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(binaryContent.size()))
          .body(resource);
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.notFound().build();
    }
  }
}
