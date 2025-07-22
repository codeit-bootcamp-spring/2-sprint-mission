package com.sprint.mission.discodeit.service.async;

import com.sprint.mission.discodeit.entity.AsyncTaskFailure;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.BinaryContentUploadStatus;
import com.sprint.mission.discodeit.entity.NotificationType;
import com.sprint.mission.discodeit.event.NotificationEvent;
import com.sprint.mission.discodeit.event.NotificationEventPublisher;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.AsyncTaskFailureRepository;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BinaryContentAsyncService {

    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentStorage binaryContentStorage;
    private final AsyncTaskFailureRepository asyncTaskFailureRepository;
    private final NotificationEventPublisher notificationEventPublisher;

    @Async("asyncExecutor")
    @Retryable(
        value = {Exception.class},
        maxAttempts = 3,
        backoff = @Backoff(delay = 2000)
    )
    @Transactional
    public void uploadFile(UUID binaryContentId, byte[] bytes) {
        BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId)
            .orElseThrow(() -> new DiscodeitException(ErrorCode.BINARY_CONTENT_NOT_FOUND));

        try {
            binaryContentStorage.put(binaryContentId, bytes);
            binaryContent.setUploadStatus(BinaryContentUploadStatus.SUCCESS);
        } catch (Exception e) {
            throw e;
        }
    }

    @Recover
    public void recover(Exception e, UUID binaryContentId, byte[] bytes) {
        BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId)
            .orElseThrow(() -> new DiscodeitException(ErrorCode.BINARY_CONTENT_NOT_FOUND));

        binaryContent.setUploadStatus(BinaryContentUploadStatus.FAILED);

        notificationEventPublisher.publish(new NotificationEvent(
            binaryContent.getUploader().getId(),
            "첨부파일 업로드에 실패했습니다",
            binaryContent.getFileName() + " 업로드 실패",
            NotificationType.ASYNC_FAILED,
            null
        ));

        String requestId = MDC.get("requestId");
        asyncTaskFailureRepository.save(
            AsyncTaskFailure.builder()
                .taskName("BinaryContentUpload")
                .requestId(requestId != null ? requestId : "unknown")
                .failureReason(e.getMessage())
                .build()
        );
    }
}
