package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.core.io.Resource;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.util.UUID;

@Component
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
@RequiredArgsConstructor
public class LocalBinaryContentStorage implements BinaryContentStorage {

    @Value("${discodeit.storage.local.root-path:${user.home}/Documents/discodeit-storage}")
    private String rootPathString;

    private Path rootPath;

    private final BinaryContentRepository binaryContentRepository;

    @PostConstruct
    public void init() {
        rootPath = Paths.get(rootPathString);
        try {
            Files.createDirectories(rootPath);
        } catch (IOException e) {
            throw new RuntimeException("디렉토리 생성 실패: " + rootPath, e);
        }
    }

    @Override
    public UUID put(UUID uuid, byte[] bytes) {
        System.out.println(uuid);

        try {
            Path filePath = resolvePath(uuid);
            Files.write(filePath, bytes);
            return uuid;
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패: " + uuid, e);
        }
    }

    @Override
    public InputStream get(UUID uuid) {
        try {
            Path filePath = resolvePath(uuid);
            return Files.newInputStream(filePath);
        } catch (IOException e) {
            throw new RuntimeException("파일 읽기 실패: " + uuid, e);
        }
    }

    @Override
    public ResponseEntity<Resource> download(UUID uuid) {
        InputStream inputStream = get(uuid);
        Resource resource = new InputStreamResource(inputStream);

        BinaryContent binaryContent = binaryContentRepository.findById(uuid)
                .orElseThrow(() -> new NoSuchElementException("Binary with id " + uuid + " not found"));

        String filename = binaryContent.getFileName();
        String encodedFilename = UriUtils.encode(filename, StandardCharsets.UTF_8);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFilename + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    private Path resolvePath(UUID uuid) {
        return rootPath.resolve(uuid.toString());
    }
}
