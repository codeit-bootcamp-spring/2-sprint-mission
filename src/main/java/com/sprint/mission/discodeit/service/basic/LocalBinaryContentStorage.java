package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;

import com.sprint.mission.discodeit.service.BinaryContentStorage;
import com.sprint.mission.discodeit.service.MessageService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@jakarta.annotation.Resource

@Service
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
public class LocalBinaryContentStorage implements BinaryContentStorage {

    private static final Logger log = LoggerFactory.getLogger(LocalBinaryContentStorage.class);
    private final Path rootPath;

    public LocalBinaryContentStorage(
        @Value("${discodeit.storage.local.root-path}") String rootPathValue) {
        this.rootPath = Paths.get(rootPathValue).toAbsolutePath().normalize();

    }

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(rootPath);
            log.info("생성완료");
        } catch (IOException e) {
            throw new RuntimeException("생성 실패");
        }
    }

    private Path resolvePath(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("파일 ID는 null일 수 없습니다.");
        }
        return this.rootPath.resolve(id.toString());
    }

    @Override
    public UUID put(UUID id, byte[] bytes) {
        if (id == null || bytes == null) {
            throw new IllegalArgumentException("ID 또는 바이트 데이터는 null일 수 없습니다.");
        }
        Path destinationPath = resolvePath(id);
        log.debug("파일 저장 시도: {}", destinationPath);
        try {
            Files.write(destinationPath, bytes);
            log.info("파일 저장 성공: {}", destinationPath);
            return id;
        } catch (IOException e) {
            log.error("파일 저장 실패: {}", destinationPath, e);
            throw new RuntimeException("파일 저장 실패: " + destinationPath, e);
        }
    }

    public UUID put(UUID id, InputStream inputStream) {
        if (id == null || inputStream == null) {
            throw new IllegalArgumentException("ID 또는 InputStream은 null일 수 없습니다.");
        }
        Path destinationPath = resolvePath(id);
        log.debug("파일 스트림 저장 시도: {}", destinationPath);
        try {
            Files.copy(inputStream, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            log.info("파일 스트림 저장 성공: {}", destinationPath);
            return id;
        } catch (IOException e) {
            log.error("파일 스트림 저장 실패: {}", destinationPath, e);
            throw new RuntimeException("파일 스트림 저장 실패: " + destinationPath, e);
        }
    }

    @Override
    public InputStream get(UUID id) throws IOException {
        Path filePath = resolvePath(id);
        if (!Files.exists(filePath)) {
            throw new FileNotFoundException("요청한 파일을 찾을 수 없습니다: " + filePath);
        }
        try {
            return Files.newInputStream(filePath);
        } catch (IOException e) {
            throw new IOException("읽기 실패");
        }
    }

    @Override
    public ResponseEntity<Resource> download(BinaryContentDto binaryContentDto) {
        if (binaryContentDto == null || binaryContentDto.id() == null) {
            log.error("다운로드 요청 실패: 유효하지 않은 BinaryContentDto");
            return ResponseEntity.badRequest().build();
        }

        UUID id = binaryContentDto.id();
        try {
            InputStream inputStream = get(id);
            Resource resource = new InputStreamResource(inputStream);

            String originalFileName =
                binaryContentDto.fileName() != null ? binaryContentDto.fileName()
                    : id.toString();
            String encodedFileName = UriUtils.encode(originalFileName, StandardCharsets.UTF_8);
            String contentDisposition = "attachment; filename=\"" + encodedFileName + "\"";

            return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(binaryContentDto.contentType()))
                .contentLength(binaryContentDto.size())
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);

        } catch (FileNotFoundException e) {
            log.warn("다운로드 파일 찾기 실패: ID={}", id, e);
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            log.error("다운로드 중 파일 읽기 오류: ID={}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            log.error("다운로드 중 예상치 못한 오류: ID={}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
