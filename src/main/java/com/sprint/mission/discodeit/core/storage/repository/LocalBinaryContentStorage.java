package com.sprint.mission.discodeit.core.storage.repository;

import com.sprint.mission.discodeit.core.storage.BinaryContentException;
import com.sprint.mission.discodeit.core.storage.dto.BinaryContentDto;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
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

@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
@Component
@Slf4j
public class LocalBinaryContentStorage implements BinaryContentStoragePort {

  private final Path storagePath;

  public LocalBinaryContentStorage(
      @Value("${discodeit.storage.local.root-path}") String storagePath,
      JpaBinaryContentRepository jpaBinaryContentRepository) {
    this.storagePath = Paths.get(storagePath);
    init();
  }

  private void init() {
    if (Files.notExists(storagePath)) {
      try {
        log.info("[LocalBinaryContentStorage] Directories created");
        Files.createDirectories(storagePath);
      } catch (IOException e) {
        log.warn("[LocalBinaryContentStorage] To create Directories is failed");
        throw new DiscodeitException(ErrorCode.INTERNAL_SERVER_ERROR);
      }
    }
  }

  @Override
  public void put(UUID id, byte[] bytes) throws IOException {
    Path destination = storagePath.resolve(id.toString() + ".png");
    try (OutputStream outputStream = Files.newOutputStream(destination)) {
      outputStream.write(bytes);
      log.info("[LocalBinaryContentStorage] Image uploaded successfully : {}", id);
    } catch (IOException e) {
      throw e;
    }
  }

  @Override
  public ResponseEntity<Resource> download(BinaryContentDto binaryContentDto) {
    try {
      Path destination = storagePath.resolve(binaryContentDto.id().toString() + ".png");
      InputStream inputStream = new FileInputStream(destination.toFile());
      InputStreamResource resource = new InputStreamResource(inputStream);

      HttpHeaders headers = new HttpHeaders();
      headers.add(HttpHeaders.CONTENT_DISPOSITION,
          "inline; filename=\"" + binaryContentDto.fileName() + "\"");
      headers.setContentType(MediaType.parseMediaType(binaryContentDto.contentType()));
      headers.setContentLength(binaryContentDto.size());

      log.info("[LocalBinaryContentStorage] Image Download successfully");
      return ResponseEntity.ok().headers(headers).body(resource);
    } catch (FileNotFoundException e) {
      throw new BinaryContentException(ErrorCode.INTERNAL_SERVER_ERROR);
    }
  }
}
