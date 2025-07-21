package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.file.FileException;
import com.sprint.mission.discodeit.exception.file.FileNotFoundException;
import com.sprint.mission.discodeit.exception.file.FileProcessingCustomException;

import jakarta.annotation.PostConstruct;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

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
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Service
@Profile("local")
public class LocalBinaryContentStorageImpi implements BinaryContentStorage {

    private static final Logger log = LoggerFactory.getLogger(LocalBinaryContentStorageImpi.class);
    private final Path rootPath;
    private final AsyncTaskFailureRepository asyncTaskFailureRepository;
    private final Executor threadPoolTaskExecutor;

    public LocalBinaryContentStorageImpi(
            @Value("${discodeit.storage.local.root-path}") String rootPathValue,
            AsyncTaskFailureRepository asyncTaskFailureRepository,
            Executor threadPoolTaskExecutor) {
        this.rootPath = Paths.get(rootPathValue).toAbsolutePath().normalize();
        this.asyncTaskFailureRepository = asyncTaskFailureRepository;
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
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

    @Override
    public CompletableFuture<Void> put(UUID id, byte[] bytes) {
        return CompletableFuture.runAsync(() -> {
                    if (id == null || bytes == null) {
                        throw new FileException(ErrorCode.FILE_NOT_FOUND);
                    }

                    Path destinationPath = resolvePath(id);

                    try {
                        Path parentDir = destinationPath.getParent();

                        if (!Files.exists(parentDir)) {
                            Files.createDirectories(parentDir);
                            log.info("💾 상위 디렉토리 생성 완료: {}", parentDir);
                        }

                        Files.write(destinationPath, bytes);

                    } catch (IOException e) {
                        throw new FileProcessingCustomException();
                    }
                },
                threadPoolTaskExecutor);
    }

    @Override
    public InputStream get(UUID id) {
        Path filePath = resolvePath(id);
        if (!Files.exists(filePath)) {
            throw new FileNotFoundException();
        }
        try {
            return Files.newInputStream(filePath);
        } catch (IOException e) {
            throw new FileProcessingCustomException();
        }
    }

    @Override
    public void delete(UUID id) {
        Path filePath = resolvePath(id);
        if (!Files.exists(filePath)) {
            throw new FileNotFoundException();
        }

        try {
            Files.delete(filePath);
            log.info("파일 삭제 성공: {}", filePath);
        } catch (IOException e) {
            throw new FileProcessingCustomException();
        }
    }

    @Override
    public ResponseEntity<Resource> download(BinaryContentDto metaData) {


        InputStream inputStream = get(metaData.id());

        Resource resource = new InputStreamResource(inputStream);


        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + metaData.fileName() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, metaData.contentType())
                .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(metaData.size()))
                .body(resource);
    }


    public AsyncTaskFailureRepository getAsyncTaskFailureRepository() {
        return asyncTaskFailureRepository;
    }
}
