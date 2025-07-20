package com.sprint.mission.discodeit.storage.local;

import com.sprint.mission.discodeit.config.MDCLoggingInterceptor;
import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.entity.AsyncTaskFailure;
import com.sprint.mission.discodeit.entity.NotificationType;
import com.sprint.mission.discodeit.event.NotificationEvent;
import com.sprint.mission.discodeit.event.NotificationEventPublisher;
import com.sprint.mission.discodeit.repository.AsyncTaskFailureRepository;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
@Component
public class LocalBinaryContentStorage implements BinaryContentStorage {

    private final Path root;
    private final AsyncTaskFailureRepository asyncTaskFailureRepository;
    private final NotificationEventPublisher notificationEventPublisher;

    public LocalBinaryContentStorage(
        @Value("${discodeit.storage.local.root-path}") Path root,
        AsyncTaskFailureRepository asyncTaskFailureRepository,
        NotificationEventPublisher notificationEventPublisher
    ) {
        this.root = root;
        this.asyncTaskFailureRepository = asyncTaskFailureRepository;
        this.notificationEventPublisher = notificationEventPublisher;
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

    public UUID put(UUID binaryContentId, byte[] bytes) {
        Path filePath = resolvePath(binaryContentId);
        if (Files.exists(filePath)) {
            throw new IllegalArgumentException(
                "File with key " + binaryContentId + " already exists");
        }
        try (OutputStream outputStream = Files.newOutputStream(filePath)) {
            outputStream.write(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return binaryContentId;
    }

    @Override
    @Retryable(
        value = {IOException.class, RuntimeException.class},
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000)
    )
    @Async("binaryContentTaskExecutor")
    public CompletableFuture<UUID> putAsync(UUID binaryContentId, byte[] bytes) {
        return CompletableFuture.completedFuture(put(binaryContentId, bytes));
    }

    @Recover
    public CompletableFuture<UUID> recover(Exception e, UUID binaryContentId, byte[] bytes) {
        String taskName = "putAsync";
        String requestId = Optional.ofNullable(MDC.get(MDCLoggingInterceptor.REQUEST_ID))
            .orElse("UNKNOWN");
        String failureReason = String.format(
            "파일 업로드 실패 - binaryContentId: %s - %s",
            binaryContentId,
            e.getMessage());

        AsyncTaskFailure failure = new AsyncTaskFailure(taskName, requestId, failureReason);
        asyncTaskFailureRepository.save(failure);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null
            && authentication.getPrincipal() instanceof DiscodeitUserDetails principal) {
            UUID receiverId = principal.getUserDto().id();

            notificationEventPublisher.publish(new NotificationEvent(
                receiverId,
                NotificationType.ASYNC_FAILED,
                null,
                "비동기 작업 실패",
                "요청하신 파일 업로드 작업이 실패했습니다."
            ));
        }

        log.error("파일 업로드 실패 - binaryContentId: {}", binaryContentId, e);
        return CompletableFuture.failedFuture(e);
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
}
