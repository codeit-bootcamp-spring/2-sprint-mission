package com.sprint.mission.discodeit.storage.local;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class LocalBinaryContentStorage implements BinaryContentStorage {

  private final Path root;

  private LocalBinaryContentStorage(@Value("${discodeit.storage.local.root-path}") Path root) {
    this.root = root;
  }

  @PostConstruct
  private void init() {
    try {
      if (!Files.exists(this.root)) {
        Files.createDirectories(this.root);
      }
    } catch (IOException e) {
      throw new RuntimeException("루트 디렉토리 초기화 실패" + e);
    }
  }

  private Path resolvePath(UUID id) {
    return root.resolve(id.toString());
  }

  @Override
  public UUID put(UUID id, byte[] bytes) {
    try (OutputStream os = Files.newOutputStream(resolvePath(id))) {
      os.write(bytes);
    } catch (IOException e) {
      throw new RuntimeException("파일 저장 실패 : ", e);
    }
    return id;
  }

  @Override
  public InputStream get(UUID id) {
    try (InputStream is = Files.newInputStream(resolvePath(id))) {
      return is;
    } catch (IOException e) {
      throw new RuntimeException("파일을 불러오는 중 오류 발생 : ", e);
    }
  }

  @Override
  public ResponseEntity<?> download(BinaryContentDto binaryContentDto) {
    try {
      InputStream is = get(binaryContentDto.id());
      return ResponseEntity.ok()
          .body(is);
    } catch (Exception e) {
      return ResponseEntity.internalServerError().body("파일 다운로드 실패 : " + e.getMessage());
    }
  }
}
