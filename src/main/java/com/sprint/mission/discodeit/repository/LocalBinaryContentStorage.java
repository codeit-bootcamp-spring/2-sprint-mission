package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;

import com.sprint.mission.discodeit.service.BinaryContentStorage;
import jakarta.annotation.PostConstruct;
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
import java.util.UUID;

import com.sprint.mission.discodeit.exception.file.FileNotFoundCustomException;
import com.sprint.mission.discodeit.exception.file.FileProcessingCustomException;

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
            log.info("저장소 디렉토리 생성 완료: {}", rootPath);
        } catch (IOException e) {
            log.error("저장소 디렉토리 생성 실패: {}", rootPath, e);
            throw new FileProcessingCustomException("initialize storage", rootPath.toString(), "저장소 디렉토리 생성 실패");
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
            throw new FileProcessingCustomException("save", destinationPath.toString(), "파일 저장 실패");
        }
    }

    @Override
    public InputStream get(UUID id) throws IOException {
        Path filePath = resolvePath(id);
        if (!Files.exists(filePath)) {
            log.warn("요청한 파일을 찾을 수 없음: {}", filePath);
            throw new FileNotFoundCustomException(filePath.toString(), "요청한 파일을 찾을 수 없음");
        }
        try {
            log.debug("파일 스트림 열기 시도: {}", filePath);
            return Files.newInputStream(filePath);
        } catch (IOException e) {
            log.error("파일 읽기 실패: {}", filePath, e);
            throw new FileProcessingCustomException("read", filePath.toString(), "파일 읽기 실패");
        }
    }

    @Override
    public void delete(UUID id) {
        Path filePath = resolvePath(id);
        if (!Files.exists(filePath)) {
            log.warn("삭제할 파일을 찾을 수 없음: {}", filePath);
            throw new FileNotFoundCustomException(filePath.toString(), "삭제할 파일을 찾을 수 없음");
        }

        try {
            log.debug("파일 삭제 시도: {}", filePath);
            Files.delete(filePath);
            log.info("파일 삭제 성공: {}", filePath);
        } catch (IOException e) {
            log.error("파일 삭제 실패: {}", filePath, e);
            throw new FileProcessingCustomException("delete", filePath.toString(), "파일 삭제 실패");
        }
    }

    @Override
    public ResponseEntity<Resource> download(BinaryContentDto binaryContentDto) {
        if (binaryContentDto == null || binaryContentDto.id() == null) {
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

        } catch (FileNotFoundCustomException e) {
            log.warn("다운로드 실패 (파일 없음): ID {} (파일명: {}). 상세: {}", id, binaryContentDto.fileName(), e.getMessage());
            throw e;
        } catch (FileProcessingCustomException e) {
            log.error("다운로드 실패 (파일 처리 오류): ID {} (파일명: {}). 상세: {}", id, binaryContentDto.fileName(), e.getMessage(), e);
            throw e;
        } catch (IOException e) {
            log.error("다운로드 실패 (IOException): ID {} (파일명: {}). 상세: {}", id, binaryContentDto.fileName(), e.getMessage(), e);
            throw new FileProcessingCustomException("download-io", id.toString(), "다운로드 준비 중 IOException 발생");
        } catch (Exception e) {
            log.error("다운로드 실패 (예상치 못한 오류): ID {} (파일명: {}). 상세: {}", id, binaryContentDto.fileName(), e.getMessage(), e);
            throw new FileProcessingCustomException("download-unexpected", id.toString(), "다운로드 중 예상치 못한 오류 발생");
        }
    }


}
