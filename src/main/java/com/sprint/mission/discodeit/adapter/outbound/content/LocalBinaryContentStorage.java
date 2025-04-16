package com.sprint.mission.discodeit.adapter.outbound.content;

import com.sprint.mission.discodeit.adapter.inbound.content.response.BinaryContentResponse;
import com.sprint.mission.discodeit.core.content.entity.BinaryContent;
import com.sprint.mission.discodeit.core.content.port.BinaryContentMetaRepositoryPort;
import com.sprint.mission.discodeit.core.content.port.BinaryContentStoragePort;
import com.sprint.mission.discodeit.exception.content.BinaryContentErrors;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
@Component
public class LocalBinaryContentStorage implements BinaryContentStoragePort {

  private final BinaryContentMetaRepositoryPort binaryContentRepository;
  private final Path storagePath;

  public LocalBinaryContentStorage(
      @Value("${discodeit.storage.local.root-path}") String storagePath,
      JpaBinaryContentRepository jpaBinaryContentRepository) {
    this.storagePath = Paths.get(storagePath);
    binaryContentRepository = new BinaryContentMetaRepositoryAdapter(jpaBinaryContentRepository);
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
    BinaryContent binaryContent = binaryContentRepository.findById(id).orElseThrow(
        () -> BinaryContentErrors.binaryContentNotFoundError(id)
    );
    String extension = binaryContent.getExtension();

    return storagePath.resolve(id.toString() + extension);
  }

  @Override
  public UUID put(UUID id, byte[] bytes) {
    Path destination = resolvePath(id);
    try (OutputStream outputStream = Files.newOutputStream(destination)) {
      outputStream.write(bytes);
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
        "attachments; filename=\"" + binaryContentResponse.fileName() + "\"");
    headers.setContentType(MediaType.parseMediaType(binaryContentResponse.contentType()));
    headers.setContentLength(binaryContentResponse.size());

    return ResponseEntity.ok().headers(headers).body(resource);
  }
}
