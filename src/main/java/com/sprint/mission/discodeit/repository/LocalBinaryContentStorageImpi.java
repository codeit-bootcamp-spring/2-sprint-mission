package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.file.FileException;
import com.sprint.mission.discodeit.exception.file.FileNotFoundCustomException;
import com.sprint.mission.discodeit.exception.file.FileProcessingCustomException;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
public class LocalBinaryContentStorageImpi implements BinaryContentStorage {

    private static final Logger log = LoggerFactory.getLogger(LocalBinaryContentStorageImpi.class);
    private final Path rootPath;

    public LocalBinaryContentStorageImpi(
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
            throw new FileProcessingCustomException();
        }
    }

    private Path resolvePath(UUID id) {
        if (id == null) {
            throw new FileException(ErrorCode.FILE_NOT_FOUND);
        }
        return this.rootPath.resolve(id.toString());
    }

    private Path resolvePath(UUID id, String originalFileName) {
        if (id == null) {
            throw new FileException(ErrorCode.FILE_NOT_FOUND);
        }
        
        String fileExtension = "";
        if (originalFileName != null && originalFileName.contains(".")) {
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }
        
        return this.rootPath.resolve(id.toString() + fileExtension);
    }

    @Override
    public void put(UUID id, byte[] bytes) {
        if (id == null || bytes == null) {
            log.error("💥 파일 저장 실패 - ID나 bytes가 null: ID={}, bytes length={}", 
                     id, bytes != null ? bytes.length : "null");
            throw new FileException(ErrorCode.FILE_NOT_FOUND);
        }
        
        Path destinationPath = resolvePath(id);
        log.info("💾 파일 저장 시도 시작");
        log.info("💾 Root Path: {}", rootPath);
        log.info("💾 Destination Path: {}", destinationPath);
        log.info("💾 파일 크기: {} bytes", bytes.length);
        log.info("💾 Root Path 존재?: {}", Files.exists(rootPath));
        
        try {
            // 상위 디렉토리 확인/생성
            Path parentDir = destinationPath.getParent();
            log.info("💾 Parent Directory: {}", parentDir);
            log.info("💾 Parent Directory 존재?: {}", Files.exists(parentDir));
            
            if (!Files.exists(parentDir)) {
                log.info("💾 상위 디렉토리 생성 시도: {}", parentDir);
                Files.createDirectories(parentDir);
                log.info("💾 상위 디렉토리 생성 완료: {}", parentDir);
            }
            
            log.info("💾 실제 파일 쓰기 시작...");
            Files.write(destinationPath, bytes);
            
            // 저장 후 검증
            if (Files.exists(destinationPath)) {
                long actualSize = Files.size(destinationPath);
                log.info("✅ 파일 저장 성공!");
                log.info("✅ 저장된 파일 경로: {}", destinationPath);
                log.info("✅ 예상 크기: {} bytes, 실제 크기: {} bytes", bytes.length, actualSize);
                log.info("✅ 크기 일치?: {}", bytes.length == actualSize);
            } else {
                log.error("💥 파일 저장 후 파일이 존재하지 않음: {}", destinationPath);
            }
            
        } catch (IOException e) {
            log.error("💥 파일 저장 실패: {}", destinationPath, e);
            log.error("💥 IOException 세부사항: {}", e.getMessage());
            throw new FileProcessingCustomException();
        }
    }

    @Override
    public InputStream get(UUID id) {
        Path filePath = resolvePath(id);
        if (!Files.exists(filePath)) {
            log.warn("요청한 파일을 찾을 수 없음: {}", filePath);
            throw new FileNotFoundCustomException();
        }
        try {
            log.debug("파일 스트림 열기 시도: {}", filePath);
            return Files.newInputStream(filePath);
        } catch (IOException e) {
            log.error("파일 읽기 실패: {}", filePath, e);
            throw new FileProcessingCustomException();
        }
    }

    @Override
    public void delete(UUID id) {
        Path filePath = resolvePath(id);
        if (!Files.exists(filePath)) {
            throw new FileNotFoundCustomException();
        }

        try {
            log.debug("파일 삭제 시도: {}", filePath);
            Files.delete(filePath);
            log.info("파일 삭제 성공: {}", filePath);
        } catch (IOException e) {
            log.error("파일 삭제 실패: {}", filePath, e);
            throw new FileProcessingCustomException();
        }
    }

    @Override
    public ResponseEntity<Resource> download(BinaryContentDto metaData) {
        log.info("💾 스토리지 다운로드 시작 - ID: {}, 파일명: {}, 크기: {}, 타입: {}", 
                 metaData.id(), metaData.fileName(), metaData.size(), metaData.contentType());
        
        InputStream inputStream = get(metaData.id());
        log.info("💾 파일 스트림 생성 완료");
        
        Resource resource = new InputStreamResource(inputStream);
        log.info("💾 리소스 생성 완료");

        ResponseEntity<Resource> response = ResponseEntity
            .status(HttpStatus.OK)
            .header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + metaData.fileName() + "\"")
            .header(HttpHeaders.CONTENT_TYPE, metaData.contentType())
            .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(metaData.size()))
            .body(resource);
            
        log.info("💾 응답 생성 완료 - 파일명: {}, 타입: {}, 크기: {}", 
                 metaData.fileName(), metaData.contentType(), metaData.size());
        
        return response;
    }


}
