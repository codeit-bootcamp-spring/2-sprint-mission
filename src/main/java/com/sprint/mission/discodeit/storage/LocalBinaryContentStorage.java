package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.service.binarycontent.FindBinaryContentResult;
import com.sprint.mission.discodeit.exception.file.DuplicateFilePathException;
import com.sprint.mission.discodeit.exception.file.FileDeleteException;
import com.sprint.mission.discodeit.exception.file.FileNotFoundException;
import com.sprint.mission.discodeit.exception.file.FileReadException;
import com.sprint.mission.discodeit.exception.file.FileWriterException;
import com.sprint.mission.discodeit.exception.file.InitDirectoryException;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
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

@Component
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
@Slf4j
public class LocalBinaryContentStorage implements BinaryContentStorage {

  @Value(value = "${discodeit.storage.local.root-path}")
  private Path root;


  @PostConstruct
  private void init() {
    try {
      Files.createDirectories(root);
    } catch (IOException e) {
      log.error("Directory init failed: (root: {})", root);
      throw new InitDirectoryException(Map.of("root", root));
    }
  }


  @Override
  public UUID put(UUID binaryContentId, byte[] bytes) {
    Path filePath = resolvePath(binaryContentId);
    if (Files.exists(filePath)) {
      log.warn("File write failed: duplicate file path (filepath: {})", filePath);
      throw new DuplicateFilePathException(Map.of("filePath", filePath));
    }
    try {
      Files.write(filePath, bytes);
      return binaryContentId;
    } catch (IOException e) {
      log.error("File write failed (binaryContentId: {}, path: {}, size: {})", binaryContentId,
          filePath, bytes.length);
      throw new FileWriterException(
          Map.of("binaryContentId", binaryContentId, "path", filePath, "size", bytes.length));
    }
  }

  @Override
  public InputStream get(UUID binaryContentId) {
    Path filePath = resolvePath(binaryContentId);
    if (Files.notExists(filePath)) {
      log.warn("File read failed: file not found (filepath: {})", filePath);
      throw new FileNotFoundException(Map.of("filePath", filePath));
    }
    try {
      return Files.newInputStream(filePath);
    } catch (IOException e) {
      log.error("File read failed (binaryContentId: {}, path: {})", binaryContentId, filePath);
      throw new FileReadException(Map.of("binaryContentId", binaryContentId, "path", filePath));
    }
  }

  @Override
  public void delete(UUID binaryContentId) {
    Path filePath = resolvePath(binaryContentId);
    try {
      Files.deleteIfExists(filePath);
    } catch (IOException e) {
      log.error("File delete failed (binaryContentId: {}, path: {})", binaryContentId, filePath);
      throw new FileDeleteException(Map.of("binaryContentId", binaryContentId, "path", filePath));
    }
  }

  @Override
  public ResponseEntity<Resource> download(FindBinaryContentResult findBinaryContentResult) {
    // 대용량 파일을 한번에 넘기면 부하가 크므로, inputStream에 담음
    InputStream inputStream = get(findBinaryContentResult.id());
    // InputStream을 Resource로 감싸서 넘기면, 스프링이 자동으로 스트리밍 다운로드 처리를 해줌 (다운로드 진행바 생각)
    // 대용량 파일을 조각조각 읽기(InputStream) → 브라우저에 실시간 전송(Resource로 감싸서 Spring이 스트리밍 처리)
    Resource resource = new InputStreamResource(inputStream);

    String encodedFilename = URLEncoder.encode(findBinaryContentResult.filename(),
            StandardCharsets.UTF_8)
        .replaceAll("\\+", "%20"); // 공백 처리

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION,
            "inline; filename=\"" + encodedFilename + "\"")
        // 프론트단에서 BinaryContent download API를 날려 사진을 가져와 바로 렌더링하는 것으로 보임 -> 이를 위해 inline으로 지정
        .contentType(MediaType.parseMediaType(findBinaryContentResult.contentType()))
        .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(findBinaryContentResult.size()))
        .body(resource);
  }

  private Path resolvePath(UUID id) {
    return root.resolve(id.toString());
  }


}
