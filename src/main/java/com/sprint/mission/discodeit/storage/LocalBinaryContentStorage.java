package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.file.BinaryContentDto;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

@Component
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
public class LocalBinaryContentStorage implements BinaryContentStorage {

    private final Path root;

    public LocalBinaryContentStorage(
        @Value("${discodeit.storage.local.root-path}") String rootPath) {
        this.root = Path.of(rootPath);
    }

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("파일 저장소 루트 디렉토리 생성 실패: " + root, e);
        }
    }

    @Override
    public UUID put(UUID id, byte[] bytes) {
        Path path = resolvePath(id);
        try (OutputStream out = Files.newOutputStream(path, StandardOpenOption.CREATE,
            StandardOpenOption.TRUNCATE_EXISTING)) {
            out.write(bytes);
            return id;
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패: " + path, e);
        }
    }

    @Override
    public InputStream get(UUID id) {
        Path path = resolvePath(id);
        try {
            return Files.newInputStream(path, StandardOpenOption.READ);
        } catch (IOException e) {
            throw new RuntimeException("파일 읽기 실패: " + path, e);
        }
    }

    @Override
    public ResponseEntity<Resource> download(BinaryContentDto dto) {
        UUID id = dto.id();
        Path path = resolvePath(id);

        try {
            InputStream inputStream = Files.newInputStream(path, StandardOpenOption.READ);
            InputStreamResource resource = new InputStreamResource(inputStream);

            String contentType =
                dto.contentType() != null ? dto.contentType() : "application/octet-stream";

            return ResponseEntity.ok()
                .contentLength(dto.size())
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + (dto.fileName() != null ? dto.fileName() : "file")
                        + "\"")
                .body(resource);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("파일 다운로드 실패: " + path, e);
        }
    }

    public Path resolvePath(UUID id) {
        return root.resolve(id.toString());
    }
}
