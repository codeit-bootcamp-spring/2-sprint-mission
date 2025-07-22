package com.sprint.mission.discodeit.service.basic;

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
import com.sprint.mission.discodeit.service.BinaryContentAsyncService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicBinaryContentAsyncService implements BinaryContentAsyncService {

    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentStorage binaryContentStorage;
    private final AsyncTaskFailureRepository asyncTaskFailureRepository;
    private final NotificationEventPublisher notificationEventPublisher;

    @Override
    @Async("contextAwareTaskExecutor")
    @Retryable(
            value = { RuntimeException.class },        // 어떤 예외를 재시도할지
            maxAttempts = 3,                          // 최대 재시도 횟수(=최초 1회 + 2회 재시도 = 3회)
            backoff = @Backoff(delay = 2000)          // 재시도 간 대기 시간(ms)
    )
    @Transactional
    public void uploadFile(UUID binaryContentId, byte[] bytes) {
        BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId)
                .orElseThrow(() -> new DiscodeitException(ErrorCode.BINARY_CONTENT_NOT_FOUND));

        try {
            // 업로드 시도
            binaryContentStorage.put(binaryContentId, bytes);
            binaryContent.setUploadStatus(BinaryContentUploadStatus.SUCCESS);
        } catch (Exception e) {
            // 재시도를 위해 예외 던지고 WAITING
            binaryContent.setUploadStatus(BinaryContentUploadStatus.WAITING);
            throw e;
        }
    }

    // 재시도 모두 실패 시 실행되는 복구 메서드 (@Recover)
    @Recover
    public void recover(RuntimeException e, UUID binaryContentId, byte[] bytes) {

        BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId)
                .orElse(null);

        if (binaryContent != null) {
            binaryContent.setUploadStatus(BinaryContentUploadStatus.FAILED);

            UUID userId = binaryContent.getUploader().getId();

            notificationEventPublisher.publish(new NotificationEvent(
                    userId,
                    "파일 업로드 실패",
                    "파일 업로드에 실패했어요. 다시 시도해주세요.",
                    NotificationType.ASYNC_FAILED,
                    null
            ));
        }

        // MDC의 RequestId 포함하여 실패 정보 기록
        String requestId = MDC.get("RequestId");
        asyncTaskFailureRepository.save(
                AsyncTaskFailure.builder()
                        .taskName("BinaryContentUpload")
                        .requestId(requestId != null ? requestId : "UNKNOWN")
                        .failureReason(e.getMessage())
                        .failedAt(Instant.now())
                        .build()
        );
    }
}
