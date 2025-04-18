package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.service.binarycontent.FindBinaryContentResult;
import com.sprint.mission.discodeit.exception.RestExceptions;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
public class LocalBinaryContentStorage implements BinaryContentStorage {

  @Value(value = "${discodeit.storage.local.root-path}")
  private Path root;

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  @PostConstruct
  private void init() {
    try {
      Files.createDirectories(root);
    } catch (IOException e) {
      throw RestExceptions.INIT_DIRECTORY_ERROR;
    }
  }


  @Override
  public UUID put(UUID binaryContentId, byte[] bytes) {
    Path filePath = resolvePath(binaryContentId);
    try {
      Files.write(filePath, bytes);
      return binaryContentId;
    } catch (IOException e) {
      logger.error("파일 저장 실패: {}", binaryContentId, e);
      throw RestExceptions.FILE_WRITE_ERROR;
    }
  }

  @Override
  public InputStream get(UUID binaryContentId) {
    Path filePath = resolvePath(binaryContentId);
    try {
      return Files.newInputStream(filePath);
    } catch (IOException e) {
      logger.error("파일 읽기 실패: {}", binaryContentId, e);
      throw RestExceptions.FILE_READ_ERROR;
    }
  }

  @Override
  public void delete(UUID binaryContentId) {
    Path filePath = resolvePath(binaryContentId);
    try {
      Files.deleteIfExists(filePath);
    } catch (IOException e) {
      logger.error("파일 삭제 실패: {}", binaryContentId, e);
      throw RestExceptions.FILE_DELETE_ERROR;
    }
  }

  @Override
  public ResponseEntity<Resource> download(FindBinaryContentResult findBinaryContentResult) {
    try {
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
    } catch (InvalidMediaTypeException e) {
      throw RestExceptions.UNSUPPORTED_MEDIA_TYPE;
    }
  }

  private Path resolvePath(UUID id) {
    return root.resolve(id.toString());
  }


}
