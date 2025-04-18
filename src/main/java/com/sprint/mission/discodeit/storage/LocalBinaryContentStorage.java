package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.StorageException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

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
        } catch (IOException e) {
            throw new RuntimeException("BinaryContent 디렉토리 생성을 실패했습니다: " + e.getMessage());
        }
    }

    @Override
    public UUID put(UUID binaryContentId, byte[] bytes) {
        try {
            Path filePath = resolvePath(binaryContentId);
            Files.write(filePath, bytes);
            return binaryContentId;
        } catch (IOException e) {
            throw new StorageException(ErrorCode.BINARY_CONTENT_SAVE_FAILED);
        }
    }

    @Override
    public UUID delete(UUID binaryContentId) {
        try {
            Path filePath = resolvePath(binaryContentId);
            Files.deleteIfExists(filePath);
            return binaryContentId;
        } catch (IOException e) {
            throw new StorageException(ErrorCode.BINARY_CONTENT_DELETE_FAILED);
        }
    }

    @Override
    public InputStream get(UUID binaryContentId) {
        try {
            Path filePath = resolvePath(binaryContentId);
            return Files.newInputStream(filePath);
        } catch (IOException e) {
            throw new StorageException(ErrorCode.BINARY_CONTENT_LOAD_FAILED);
        }
    }

    @Override
    public ResponseEntity<Resource> download(BinaryContentDto binaryContentDto) {
        try (InputStream inputStream = get(binaryContentDto.id())) {
            byte[] content = StreamUtils.copyToByteArray(inputStream);
            ByteArrayResource resource = new ByteArrayResource(content);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(binaryContentDto.contentType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + binaryContentDto.fileName() + "\"")
                    .body(resource);
        } catch (IOException e) {
            throw new StorageException(ErrorCode.BINARY_CONTENT_DOWNLOAD_FAILED);
        }
    }

    private Path resolvePath(UUID fileBinaryContentId) {
        String FILE_EXTENSION = ".ser";
        return root.resolve(fileBinaryContentId + FILE_EXTENSION);
    }
}
