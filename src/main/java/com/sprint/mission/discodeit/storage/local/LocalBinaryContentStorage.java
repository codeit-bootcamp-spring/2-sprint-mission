package com.sprint.mission.discodeit.storage.local;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.exception.storage.BinaryStorageDownloadException;
import com.sprint.mission.discodeit.exception.storage.BinaryStorageGetException;
import com.sprint.mission.discodeit.exception.storage.BinaryStorageMakeDirException;
import com.sprint.mission.discodeit.exception.storage.BinaryStoragePutException;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(
    name = "discodeit.storage.type",
    havingValue = "local"
)
public class LocalBinaryContentStorage implements BinaryContentStorage {

    private final Path root;

    private LocalBinaryContentStorage(@Value("${discodeit.storage.local.root-path}") Path root) {
        this.root = root;
    }

    @PostConstruct
    private void init() {
        try {
            if (!Files.exists(this.root)) {
                Files.createDirectories(this.root);
            }
        } catch (IOException e) {
            throw BinaryStorageMakeDirException.from(root.toString(), e);
        }
    }

    private Path resolvePath(UUID id) {
        return root.resolve(id.toString());
    }

    @Override
    @Async("fileUploadExecutor")
    public CompletableFuture<UUID> put(UUID id, byte[] bytes) {
        String threadName = Thread.currentThread().getName();
        log.info("파일 업로드 진행 시작: id={}, thread={}", id, threadName);
        try (OutputStream os = Files.newOutputStream(resolvePath(id))) {
            log.info("파일 업로드 진행중: id={}, thread={}", id, threadName);
            os.write(bytes);
            log.info("파일 업로드 완료: id={}, thread={}", id, threadName);
        } catch (IOException e) {
            log.info("파일 업로드 실패: id={}, thread={}", id, threadName);
            throw BinaryStoragePutException.forId(id.toString(), e);
        }
        return CompletableFuture.completedFuture(id);
    }

    @Override
    public InputStream get(UUID id) {
        try {
            return Files.newInputStream(resolvePath(id));
        } catch (IOException e) {
            throw BinaryStorageGetException.forId(id.toString(), e);
        }
    }

    @Override
    public ResponseEntity<Resource> download(BinaryContentDto binaryContentDto) {
        try {
            InputStream is = get(binaryContentDto.id());
            InputStreamResource resource = new InputStreamResource(is);

            String fileName = binaryContentDto.fileName();
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20");

            String contentDisposition =
                "attachment; filename=\"download\"; filename*=UTF-8''" + encodedFileName;

            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .contentType(MediaType.parseMediaType(binaryContentDto.contentType()))
                .contentLength(binaryContentDto.size())
                .body(resource);
        } catch (Exception e) {
            throw BinaryStorageDownloadException.forId(binaryContentDto.id().toString(), e);
        }
    }
}
