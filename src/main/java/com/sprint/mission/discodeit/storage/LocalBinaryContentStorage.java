package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.service.binarycontent.BinaryContentResult;
import com.sprint.mission.discodeit.util.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Path;
import java.util.UUID;

@Component
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
public class LocalBinaryContentStorage implements BinaryContentStorage {

    private final Path rootPath;

    public LocalBinaryContentStorage(@Value("${discodeit.storage.local.root-path}") Path rootPath) {
        this.rootPath = rootPath;
        initDirectory(rootPath);
    }

    @Override
    public UUID put(UUID binaryContentId, byte[] bytes) {
        if (binaryContentId == null) {
            throw new IllegalArgumentException("ID는 null일 수 없습니다.");
        }
        if (bytes == null) {
            throw new IllegalArgumentException("content bytes는 null일 수 없습니다.");
        }
        saveBinaryContentFile(binaryContentId, bytes);

        return binaryContentId;
    }

    private void saveBinaryContentFile(UUID binaryContentId, byte[] bytes) {
        Path filePath = resolvePath(binaryContentId);
        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath.toFile())) {
            fileOutputStream.write(bytes);
        } catch (IOException e) {
            throw new UncheckedIOException("파일에 저장하는 작업을 실패했습니다.", e);
        }
    }

    @Override
    public InputStream get(UUID binaryContentId) {
        Path filePath = resolvePath(binaryContentId);

        try {
            return new FileInputStream(filePath.toFile());
        } catch (IOException e) {
            throw new UncheckedIOException("파일을 읽을 수 없습니다: " + filePath, e);
        }
    }

    @Override
    public ResponseEntity<?> download(BinaryContentResult binaryContentResult) {
        InputStream inputStream = get(binaryContentResult.id());

        return ResponseEntity.ok(new InputStreamResource(inputStream));
    }

    private Path resolvePath(UUID binaryContentId) {
        return rootPath.resolve(binaryContentId.toString());
    }

    private static void initDirectory(Path directoryPath) {
        if (directoryPath == null) {
            return;
        }
        FileUtils.creatDirectory(directoryPath);
    }

}
