package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.service.dto.response.BinaryContentResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local", matchIfMissing = true)
@Repository
public class LocalBinaryContentStorage implements BinaryContentStorage {

    private final Path ROOT;

    public LocalBinaryContentStorage(
            @Value("${discodeit.storage.local.root-path}") String fileRoot) {
        this.ROOT = Paths.get(System.getProperty("user.dir"), fileRoot, BinaryContentStorage.class.getSimpleName());
        init();
    }

    private void init() {
        if (!Files.exists(ROOT)) {
            try {
                Files.createDirectories(ROOT);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Path resolvePath(UUID id) {
        return ROOT.resolve(id.toString());
    }


    @Override
    public UUID put(UUID id, byte[] bytes) {
        Path path = resolvePath(id);
        try (
                FileOutputStream fos = new FileOutputStream(path.toFile());
                BufferedOutputStream bos = new BufferedOutputStream(fos);
        ) {
            bos.write(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return id;
    }

    @Override
    public InputStream get(UUID id) {
        Path path = resolvePath(id);
        try {
            return new FileInputStream(path.toFile());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<Resource> download(BinaryContentResponseDto binaryContentResponse) {
        Resource resource = new InputStreamResource(get(binaryContentResponse.id()));
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(binaryContentResponse.contentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
