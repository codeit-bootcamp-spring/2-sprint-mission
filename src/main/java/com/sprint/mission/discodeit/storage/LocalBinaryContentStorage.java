package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.exception.binaryContent.BinaryContentDeleteFailedException;
import com.sprint.mission.discodeit.exception.binaryContent.BinaryContentDownloadFailedException;
import com.sprint.mission.discodeit.exception.binaryContent.BinaryContentLoadFailedException;
import com.sprint.mission.discodeit.exception.binaryContent.BinaryContentSaveFailedException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

@Slf4j
@Component
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
public class LocalBinaryContentStorage implements BinaryContentStorage {

    private final Path root;

    public LocalBinaryContentStorage(
            @Value("${discodeit.storage.local.root-path:binary-content}") String directoryPath) {
        this.root = Paths.get(System.getProperty("user.dir"), directoryPath);
        init();
    }

    private void init() {
        try {
            Files.createDirectories(root);
            log.info("BinaryContent directory initialized at {}", root);
        } catch (IOException e) {
            log.error("Failed to create BinaryContent directory: {}", e.getMessage());
            throw new RuntimeException("BinaryContent 디렉토리 생성을 실패했습니다: " + e.getMessage());
        }
    }

    @Override
    public UUID put(UUID binaryContentId, byte[] bytes) {
        try {
            Path filePath = resolvePath(binaryContentId);
            Files.write(filePath, bytes);
            log.info("Binary content saved at {}", filePath);
            return binaryContentId;
        } catch (IOException e) {
            log.error("Failed to save binary content: {}", e.getMessage());
            throw new BinaryContentSaveFailedException(binaryContentId);
        }
    }

    @Override
    public UUID delete(UUID binaryContentId) {
        try {
            Path filePath = resolvePath(binaryContentId);
            Files.deleteIfExists(filePath);
            log.info("Binary content deleted at {}", filePath);
            return binaryContentId;
        } catch (IOException e) {
            log.error("Failed to delete binary content: {}", e.getMessage());
            throw new BinaryContentDeleteFailedException(binaryContentId);
        }
    }

    @Override
    public InputStream get(UUID binaryContentId) {
        try {
            Path filePath = resolvePath(binaryContentId);
            log.debug("Loading binary content from {}", filePath);
            return Files.newInputStream(filePath);
        } catch (IOException e) {
            log.error("Failed to load binary content: {}", e.getMessage());
            throw new BinaryContentLoadFailedException(binaryContentId);
        }
    }

    @Override
    public ResponseEntity<Resource> download(BinaryContentDto binaryContentDto) {
        try (InputStream inputStream = get(binaryContentDto.id())) {
            byte[] content = StreamUtils.copyToByteArray(inputStream);
            ByteArrayResource resource = new ByteArrayResource(content);
            log.info("Binary content ready for download: {}", binaryContentDto);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(binaryContentDto.contentType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + binaryContentDto.fileName() + "\"")
                    .body(resource);
        } catch (IOException e) {
            log.error("Failed to download binary content: {}", e.getMessage());
            throw new BinaryContentDownloadFailedException(binaryContentDto.id());
        }
    }

    private Path resolvePath(UUID fileBinaryContentId) {
        String FILE_EXTENSION = ".ser";
        return root.resolve(fileBinaryContentId + FILE_EXTENSION);
    }
}
