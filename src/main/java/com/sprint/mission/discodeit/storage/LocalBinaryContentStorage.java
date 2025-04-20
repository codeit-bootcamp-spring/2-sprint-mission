package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.config.condition.LocalStorageCondition;
import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RequiredArgsConstructor
@Component
@Conditional(LocalStorageCondition.class)
public class LocalBinaryContentStorage implements BinaryContentStorage {

  private final Path root;

  @PostConstruct
  public void init() throws IOException {
    Files.createDirectories(root);
  }

  @Override
  public UUID put(UUID id, byte[] bytes) {
    try {
      Files.write(resolvePath(id), bytes);
      return id;
    } catch (IOException e) {
      throw new UncheckedIOException("Failed to write file", e);
    }
  }

  @Override
  public InputStream get(UUID id) {
    try {
      return Files.newInputStream(resolvePath(id));
    } catch (IOException e) {
      throw new UncheckedIOException("Failed to read file", e);
    }
  }

  @Override
  public ResponseEntity<Resource> download(BinaryContentDto dto) {
    try {
      InputStream inputStream = get(dto.id());
      Resource resource = new InputStreamResource(inputStream);
      return ResponseEntity.ok()
          .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dto.fileName() + "\"")
          .contentLength(dto.size())
          .contentType(MediaType.parseMediaType(dto.contentType()))
          .body(resource);
    } catch (Exception e) {
      return ResponseEntity.notFound().build();
    }
  }

  private Path resolvePath(UUID id) {
    return root.resolve(id.toString());
  }
}