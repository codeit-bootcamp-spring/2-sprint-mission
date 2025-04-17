package com.sprint.mission.discodeit.storage.local;

import com.sprint.mission.discodeit.dto.service.binarycontent.BinaryContentDto;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.util.FileUtil;
import jakarta.annotation.PostConstruct;
import java.io.InputStream;
import java.nio.file.Path;
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

  public LocalBinaryContentStorage(@Value("${discodeit.storage.local.root-path}") Path root) {
    this.root = root;
  }

  @PostConstruct
  public void init() {
    FileUtil.init(root);
  }

  @Override
  public UUID put(UUID id, byte[] data) {
    return FileUtil.save(root, id, data);
  }

  @Override
  public InputStream get(UUID id) {
    return FileUtil.get(root, id);
  }

  @Override
  public ResponseEntity<Resource> download(BinaryContentDto contentDto) {
    InputStream downloadData = get(contentDto.id());
    Resource resource = new InputStreamResource(downloadData);

    return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .header(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + contentDto.fileName() + "\"")
        .body(resource);
  }
}
