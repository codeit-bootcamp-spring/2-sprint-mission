package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentDto;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
public class LocalBinaryContentStorage implements BinaryContentStorage {

    private Path root;

    @Value("${discodeit.storage.local.root-path}")
    private String rootPath;

    public LocalBinaryContentStorage() {
    }

    @PostConstruct
    private void init() {
        try {
            root = Paths.get(rootPath);

            if (!Files.exists(root)) {
                Files.createDirectories(root);
            }
        } catch (IOException e) {
            throw new RuntimeException("[ERROR] file init fail");
        }
    }

    @Override
    public UUID put(UUID id, byte[] data) {
        try {
            Path filePath = resolvePath(id);

            Files.createDirectories(filePath.getParent());
            Files.write(filePath, data);
            return id;
        } catch (Exception e) {
            throw new RuntimeException("[ERROR] file write fail");
        }
    }

    @Override
    public InputStream get(UUID id) {
        try {
            Path filePath = resolvePath(id);
            if (!Files.exists(filePath)) {
                return null;
            }
            return Files.newInputStream(filePath);
        } catch (IOException e) {
            throw new RuntimeException("[ERROR] file read fail");
        }
    }

    @Override
    public ResponseEntity<Resource> download(BinaryContentDto binaryContentDto) {
        try {
            UUID id = binaryContentDto.getId();
            Path filePath = resolvePath(id);

            if (!Files.exists(filePath)) {
                return ResponseEntity.notFound().build();
            }

            Resource resource = new FileSystemResource(filePath.toFile());

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + binaryContentDto.getFileName() + "\"");

            MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
            if (binaryContentDto.getContentType() != null && !binaryContentDto.getContentType()
                .isEmpty()) {
                try {
                    mediaType = MediaType.parseMediaType(binaryContentDto.getContentType());
                } catch (Exception e) {
                    throw new RuntimeException("[ERROR] media type parse error");
                }
            }

            return ResponseEntity.ok()
                .headers(headers)
                .contentType(mediaType)
                .body(resource);
        } catch (Exception e) {
            throw new RuntimeException("[ERROR] file download error");
        }
    }

    @Override
    public void deleteById(UUID id) {
        try {
            Path filePath = resolvePath(id);

            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }
        } catch (IOException e) {
            throw new RuntimeException("[ERROR] file delete fail", e);
        }
    }

    private Path resolvePath(UUID id) {
        // 파일 저장 위치 규칙: {root}/{UUID}
        return root.resolve(id.toString());
    }
}
