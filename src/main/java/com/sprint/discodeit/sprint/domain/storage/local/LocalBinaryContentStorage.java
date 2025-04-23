package com.sprint.discodeit.sprint.domain.storage.local;

import com.sprint.discodeit.sprint.domain.dto.binaryContentDto.BinaryContentDto;
import com.sprint.discodeit.sprint.domain.storage.BinaryContentStorage;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.UUID;

@RequiredArgsConstructor
@Component
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
public class LocalBinaryContentStorage implements BinaryContentStorage {

    private final LocalStorageProperties properties;
    private Path root;

    @PostConstruct
    public void init() throws IOException {
        this.root = Paths.get(properties.getRootPath());
        Files.createDirectories(root); // 디렉토리 없으면 생성
    }

    private Path resolvePath(Long id) {
        return root.resolve(id.toString());
    }

    @Override
    public Long put(Long id, byte[] data) {
        try {
            Files.write(resolvePath(id), data, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            return id;
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패", e);
        }
    }

    @Override
    public InputStream get(Long id) {
        try {
            return Files.newInputStream(resolvePath(id));
        } catch (IOException e) {
            throw new RuntimeException("파일 조회 실패", e);
        }
    }

    @Override
    public ResponseEntity<Resource> download(BinaryContentDto dto) {
        try {
            InputStream input = get(dto.id());
            Resource resource = new InputStreamResource(input);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dto.fileName() + "\"")
                    .contentType(MediaType.parseMediaType(dto.contentType()))
                    .body(resource);
        } catch (Exception e) {
            throw new RuntimeException("파일 다운로드 실패", e);
        }
    }
}