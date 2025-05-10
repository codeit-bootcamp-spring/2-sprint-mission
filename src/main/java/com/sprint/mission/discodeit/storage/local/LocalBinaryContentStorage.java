package com.sprint.mission.discodeit.storage.local;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
@Component
public class LocalBinaryContentStorage implements BinaryContentStorage {

    // 파일 저장의 기본 디렉토리 경로
    private final Path root;

    // Spring이 application.yaml로부터 경로 값을 주입
    public LocalBinaryContentStorage(
        @Value("${discodeit.storage.local.root-path}") Path root
    ) {
        this.root = root;
    }

    @PostConstruct
    public void init() {
        // 애플리케이션 시작 시점에 root 디렉토리가 존재하지 않을 경우 자동생성
        if (!Files.exists(root)) {
            try {
                Files.createDirectories(root);
            } catch (IOException e) {
                e.printStackTrace();
                throw new DiscodeitException(ErrorCode.FILE_STORAGE_ERROR,
                    Map.of("파일 저장을 위한 디렉토리 생성 도중 오류", e));
            }
        }
    }

    // 파일을 지정한 UUID 이름으로 저장
    public UUID put(UUID binaryContentId, byte[] bytes) {
        Path filePath = resolvePath(binaryContentId);
        // 해당 파일이 이미 있으면 error
        if (Files.exists(filePath)) {
            throw new DiscodeitException(ErrorCode.INFO_DUPLICATE,
                Map.of("File is already exist", binaryContentId));
        }
        // 바이너리 데이터를 파일에 쓴다.
        try (OutputStream outputStream = Files.newOutputStream(filePath)) {
            outputStream.write(bytes);
        } catch (IOException e) {
            throw new DiscodeitException(ErrorCode.FILE_STORAGE_ERROR,
                Map.of("Binary data를 file에 쓴 도중 오류", e));
        }
        return binaryContentId;
    }

    // UUID로 저장된 파일을 읽어서 InputStream 형태로 반환
    public InputStream get(UUID binaryContentId) {
        Path filePath = resolvePath(binaryContentId);
        // 존재하지 않을 경우
        if (Files.notExists(filePath)) {
            throw new DiscodeitException(ErrorCode.INFO_NOT_FOUND,
                Map.of("File is not exist", filePath));
        }
        // 읽기 스트림으로 반환
        try {
            return Files.newInputStream(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            throw new DiscodeitException(ErrorCode.FILE_STORAGE_ERROR,
                Map.of("File to InputStream 도중 오류", e));
        }
    }

    // UUID값을 기반으로 실제 파일 경로를 반환하는 Helper Method
    private Path resolvePath(UUID key) {
        return root.resolve(key.toString());
    }

    @Override
    // Client가 요청한 파일을 HTTP 다운로드 response 형태로 반환
    /*
      < 반환 구조 >
      HTTP/1.1 200 OK
      Content-Disposition: attachment; filename="파일이름"
      Content-Type: 컨텐츠타입
      Content-Length: 크기
    */
    public ResponseEntity<Resource> download(BinaryContentDto metaData) {
        InputStream inputStream = get(metaData.id());
        Resource resource = new InputStreamResource(inputStream);

        // 다운로드용 응답 구성
        return ResponseEntity
            .status(HttpStatus.OK)
            .header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + metaData.fileName() + "\"")
            .header(HttpHeaders.CONTENT_TYPE, metaData.contentType())
            .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(metaData.size()))
            .body(resource);
    }
}
/*
  < Content Type >
  타입                         설명
  application/json	          JSON 형식 응답
  text/plain	              일반 텍스트
  application/octet-stream	  이진 파일 (기본값/일반 다운로드 파일)
  image/png, image/jpeg	      이미지
  application/pdf	          PDF 문서
  application/zip	          압축 파일
*/