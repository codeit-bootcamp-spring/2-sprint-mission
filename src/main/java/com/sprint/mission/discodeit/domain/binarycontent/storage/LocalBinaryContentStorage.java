package com.sprint.mission.discodeit.domain.binarycontent.storage;

import com.sprint.mission.discodeit.domain.binarycontent.dto.BinaryContentResult;
import com.sprint.mission.discodeit.util.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
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
    }

    @Override
    public UUID put(UUID binaryContentId, byte[] bytes) {
        initDirectory(rootPath);
        validateBinaryContentId(binaryContentId);
        validateBytes(bytes);
        saveBinaryContentFile(binaryContentId, bytes);

        return binaryContentId;
    }

    @Override
    public InputStream get(UUID binaryContentId) {
        Path filePath = resolvePath(binaryContentId);

        return getFileInputStream(filePath);
    }

    @Override
    public InputStreamResource download(BinaryContentResult binaryContentResult) {
        InputStream inputStream = get(binaryContentResult.id());

        return new InputStreamResource(inputStream);
    }

    private void validateBytes(byte[] bytes) {
        if (bytes == null) {
            throw new IllegalArgumentException("content bytes는 null일 수 없습니다.");
        }
    }

    private void validateBinaryContentId(UUID binaryContentId) {
        if (binaryContentId == null) {
            throw new IllegalArgumentException("ID는 null일 수 없습니다.");
        }
    }

    private FileInputStream getFileInputStream(Path filePath) {
        try {
            return new FileInputStream(filePath.toFile());
        } catch (IOException e) {
            throw new UncheckedIOException("파일을 읽을 수 없습니다: " + filePath, e);
        }
    }

    private void saveBinaryContentFile(UUID binaryContentId, byte[] bytes) {
        Path filePath = resolvePath(binaryContentId);
        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath.toFile())) {
            fileOutputStream.write(bytes);
        } catch (IOException e) {
            throw new UncheckedIOException("파일에 저장하는 작업을 실패했습니다.", e);
        }
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
