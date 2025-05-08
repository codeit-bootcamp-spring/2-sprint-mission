package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Component
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
public class LocalBinaryContentStorage implements BinaryContentStorage {

    private final Path root;

    public LocalBinaryContentStorage(@Value("${discodeit.storage.local.root-path}") String rootDir) {
        this.root = Paths.get(rootDir);
        init();
    }

    private void init() {
        log.info("▶▶ [STORAGE] Initializing local storage - root: {}", root.toAbsolutePath());
        try {
            if (!Files.exists(root)) {
                Files.createDirectories(root);
                log.info("◀◀ [STORAGE] Local storage directory created - root: {}", root.toAbsolutePath());
            } else {
                log.info("◀◀ [STORAGE] Local storage directory already exists - root: {}", root.toAbsolutePath());
            }
        } catch (IOException e) {
            log.error("◀◀ [STORAGE] Failed to initialize storage location - root: {}", root.toAbsolutePath(), e);
            throw new RuntimeException("Failed to initialize storage location", e);
        }
    }

    @Override
    public UUID put(UUID id, byte[] bytes) {
        Path path = resolvePath(id);
        log.info("▶▶ [STORAGE] Attempting to save file - id: {}, path: {}", id, path.toAbsolutePath());
        try {
            Files.createDirectories(path.getParent());
            Files.write(path, bytes);
            log.info("◀◀ [STORAGE] File saved successfully - id: {}, size: {} bytes", id, bytes.length);
            return id;
        } catch (IOException e) {
            log.error("◀◀ [STORAGE] Failed to save file - id: {}, path: {}", id, path.toAbsolutePath(), e);
            throw new RuntimeException("Failed to save file", e);
        }
    }

    private Path resolvePath(UUID id) {
        return root.resolve(id.toString());
    }

    @Override
    public InputStream get(UUID id) {
        Path path = resolvePath(id);
        log.info("▶▶ [STORAGE] Attempting to read file - id: {}, path: {}", id, path.toAbsolutePath());
        try {
            if (!Files.exists(path)) {
                log.warn("◀◀ [STORAGE] File not found - id: {}, path: {}", id, path.toAbsolutePath());
                throw new RuntimeException("File not found.");
            }
            log.info("◀◀ [STORAGE] File read successfully - id: {}", id);
            return new FileInputStream(path.toFile());
        } catch (IOException e) {
            log.error("◀◀ [STORAGE] Failed to read file - id: {}, path: {}", id, path.toAbsolutePath(), e);
            throw new RuntimeException("Failed to read file", e);
        }
    }

    @Override
    public ResponseEntity<Resource> download(BinaryContentDto binaryContentDto) {
        UUID id = binaryContentDto.id();
        log.info("▶▶ [STORAGE] Attempting to download file - id: {}, fileName: {}", id, binaryContentDto.fileName());
        try {
            InputStream inputStream = get(id);
            InputStreamResource resource = new InputStreamResource(inputStream);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + binaryContentDto.fileName());

            MediaType mediaType;
            try {
                mediaType = MediaType.parseMediaType(binaryContentDto.contentType());
            } catch (Exception e) {
                mediaType = MediaType.APPLICATION_OCTET_STREAM;
            }

            log.info("◀◀ [STORAGE] File download response created successfully - id: {}", id);
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(mediaType)
                    .contentLength(binaryContentDto.size())
                    .body((Resource) resource);
        } catch (Exception e) {
            log.error("◀◀ [STORAGE] Failed to download file - id: {}, fileName: {}", id, binaryContentDto.fileName(), e);
            throw new RuntimeException("Failed to download file", e);
        }
    }
}