package com.sprint.mission.discodeit.storage.local;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.entity.AsyncTaskFailure;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.entity.NotificationType;
import com.sprint.mission.discodeit.entity.UploadStatus;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.AsyncTaskFailureRepository;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.notification.NotificationEventPublisher;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import io.micrometer.core.annotation.Timed;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
@Slf4j
@Component
public class LocalBinaryContentStorage implements BinaryContentStorage {

    private final Path root;
    private final AsyncTaskFailureRepository asyncTaskFailureRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final NotificationEventPublisher eventPublisher;
    private final UserRepository userRepository;

    public LocalBinaryContentStorage(
        @Value("${discodeit.storage.local.root-path}") Path root,
        AsyncTaskFailureRepository asyncTaskFailureRepository,
        BinaryContentRepository binaryContentRepository,
        NotificationEventPublisher evenPublisher,
        UserRepository userRepository
    ) {
        this.root = root;
        this.asyncTaskFailureRepository = asyncTaskFailureRepository;
        this.binaryContentRepository = binaryContentRepository;
        this.eventPublisher = evenPublisher;
        this.userRepository = userRepository;
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

    @Async("storageTaskExecutor")
    @Retryable(
        value = {IOException.class},
        maxAttempts = 3,
        backoff = @Backoff(delay = 2000) // 2초 대기 후 재시도
    )

    @Override
    public CompletableFuture<UUID> put(UUID binaryContentId, byte[] bytes) {

        Path filePath = resolvePath(binaryContentId);
        if (Files.exists(filePath)) {
            throw new IllegalArgumentException(
                "File with key " + binaryContentId + " already exists");
        }
        try (OutputStream outputStream = Files.newOutputStream(filePath)) {
            Thread.sleep(1000);
            outputStream.write(bytes);
            binaryContentRepository.findById(binaryContentId).ifPresent(binaryContent -> {
                binaryContent.setUploadStatus(UploadStatus.SUCCESS);
                binaryContentRepository.save(binaryContent);
            });
            return CompletableFuture.completedFuture(binaryContentId);
        } catch (IOException e) {
            throw new CompletionException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public InputStream get(UUID binaryContentId) {
        Path filePath = resolvePath(binaryContentId);
        if (Files.notExists(filePath)) {
            throw new NoSuchElementException(
                "File with key " + binaryContentId + " does not exist");
        }
        try {
            return Files.newInputStream(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private Path resolvePath(UUID key) {
        return root.resolve(key.toString());
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

    @Recover
    @Override
    public void recover(IOException e, BinaryContent content) {
        log.error("업로드 재시도 실패: {}", e.getMessage());

        String requestId = MDC.get("X-REQUEST-ID");
        asyncTaskFailureRepository.save(
            new AsyncTaskFailure(content.getFileName(), requestId, e.getMessage())
        );
        Notification event = new Notification(
            userRepository.findById(UUID.fromString(requestId)).orElseThrow(
                () -> new UserNotFoundException()
            )
            ,
            content.getFileName(),
            "content async failed",
            NotificationType.ASYNC_FAILED,
            null,
            LocalDateTime.now()
        );
        eventPublisher.publish(event);

        content.setUploadStatus(UploadStatus.FAILED);
        binaryContentRepository.save(content);
    }
}
