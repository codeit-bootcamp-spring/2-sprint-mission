package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.NoSuchElementException;
import java.util.UUID;

@Component
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
public class LocalBinaryContentStorage implements BinaryContentStorage {
    private final Path root;

    public LocalBinaryContentStorage(
            @Value("${discodeit.storage.local.root-path}") Path root
    ) {
        this.root = root;
    }

    @PostConstruct
    public void init() {
        if (!Files.exists(root)) {
            try {
                Files.createDirectories(root);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public UUID put(UUID binaryContentId, byte[] bytes) {
        Path path = resolvePath(binaryContentId);
        if (Files.exists(path)) {
            throw new IllegalArgumentException("File with key " + binaryContentId + " already exists");
        }
        try (OutputStream outputStream = Files.newOutputStream(path)) {
            outputStream.write(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return binaryContentId;
    }

    @Override
    public InputStream get(UUID binaryContentId) {
        Path path = resolvePath(binaryContentId);
        if (Files.notExists(path)) {
            throw new NoSuchElementException("File with key " + binaryContentId + " does not exist");
        }
        try {
            return Files.newInputStream(path);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
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


    private Path resolvePath(UUID id) {
        return root.resolve(id.toString());
    }
}
