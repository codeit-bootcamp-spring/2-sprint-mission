package com.sprint.mission.discodeit.adapter.outbound.content;

import com.sprint.mission.discodeit.adapter.inbound.content.response.BinaryContentResponse;
import com.sprint.mission.discodeit.core.content.port.BinaryContentStoragePort;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
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
public class LocalBinaryContentStorage implements BinaryContentStoragePort {

  private final Map<UUID, byte[]> storage = new ConcurrentHashMap<>();
  private final Path storagePath;

  public LocalBinaryContentStorage(
      @Value("${discodeit.storage.local.root-path}") String storagePath) {
    this.storagePath = Paths.get(storagePath);
    init();
  }

  private void init() {
    if (Files.notExists(storagePath)) {
      try {
        Files.createDirectories(storagePath);
      } catch (IOException e) {
        //TODO. 예외처리 구현해야함
        throw new RuntimeException();
      }
    }
  }

  private Path resolvePath(UUID id) {
    return storagePath.resolve(id.toString());
  }

  @Override
  public UUID put(UUID id, byte[] bytes) {
    Path destination = resolvePath(id);
    try (FileOutputStream fos = new FileOutputStream(destination.toFile());
        ObjectOutputStream oos = new ObjectOutputStream(fos)) {
      storage.put(id, bytes);
      oos.writeObject(storage);
    } catch (IOException e) {
      throw new RuntimeException();
    }
    return id;
  }

  @Override
  public InputStream get(UUID id) {
    Path destination = resolvePath(id);
    try {
      return new FileInputStream(destination.toFile());
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public ResponseEntity<Resource> download(BinaryContentResponse binaryContentResponse) {
    InputStream inputStream = get(binaryContentResponse.id());

    InputStreamResource resource = new InputStreamResource(inputStream);

    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_DISPOSITION,
        "attachment; filename=\"" + binaryContentResponse.fileName() + "\"");
    headers.setContentType(MediaType.parseMediaType(binaryContentResponse.contentType()));
    headers.setContentLength(binaryContentResponse.size());

    return ResponseEntity.ok().headers(headers).body(resource);
  }
}
