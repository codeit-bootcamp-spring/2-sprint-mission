package com.sprint.mission.discodeit.listener;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.event.AsyncFailedNotificationEvent;
import com.sprint.mission.discodeit.event.AsyncTaskFailureEvent;
import com.sprint.mission.discodeit.event.BinaryContentCreateEvent;
import com.sprint.mission.discodeit.event.BinaryContentUploadFailureEvent;
import com.sprint.mission.discodeit.event.BinaryContentUploadSuccessEvent;
import com.sprint.mission.discodeit.exception.binarycontent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class BinaryContentUploadEventListener {

  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;
  private final ApplicationEventPublisher eventPublisher;

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  @Async("binaryContentTaskExecutor")
  public void handleBinaryContentCreated(BinaryContentCreateEvent event) {
    String requestId = event.requestId();

    try {
      log.info("비동기 업로드 시작: binaryContentId={}, fileName={}, size={}",
          event.binaryContentId(), event.fileName(), event.bytes().length);

      UUID result = binaryContentStorage.put(event.binaryContentId(), event.bytes())
          .get();

      eventPublisher.publishEvent(
          new BinaryContentUploadSuccessEvent(event.binaryContentId(), requestId)
      );

      log.info("업로드 성공: binaryContentId={}", event.binaryContentId());

    } catch (Exception e) {
      log.error("업로드 실패: binaryContentId={}", event.binaryContentId(), e);

      String failureReason = String.format(
          "파일 업로드 실패 - binaryContentId: %s, fileName: %s, error: %s",
          event.binaryContentId(),
          event.fileName(),
          e.getMessage()
      );

      eventPublisher.publishEvent(
          new BinaryContentUploadFailureEvent(
              event.binaryContentId(),
              requestId,
              "uploadWithRetry",
              failureReason,
              event.userId()
          )
      );
    }
  }

  @EventListener
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void handleUploadSuccess(BinaryContentUploadSuccessEvent event) {
    log.info("업로드 성공 이벤트 처리 시작: binaryContentId={}, requestId={}",
        event.binaryContentId(), event.requestId());

    try {
      BinaryContent binaryContent = binaryContentRepository.findById(event.binaryContentId())
          .orElseThrow(() -> BinaryContentNotFoundException.withId(event.binaryContentId()));

      binaryContent.markAsSuccess();
      binaryContentRepository.save(binaryContent);

      log.info("업로드 성공 상태 업데이트 완료: binaryContentId={}, status=SUCCESS", event.binaryContentId());
    } catch (Exception e) {
      log.error("업로드 성공 이벤트 처리 중 오류 발생: binaryContentId={}", event.binaryContentId(), e);
    }
  }

  @EventListener
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void handleUploadFailure(BinaryContentUploadFailureEvent event) {
    log.info("업로드 실패 이벤트 처리 시작: binaryContentId={}, requestId={}",
        event.binaryContentId(), event.requestId());

    try {
      BinaryContent binaryContent = binaryContentRepository.findById(event.binaryContentId())
          .orElseThrow(() -> BinaryContentNotFoundException.withId(event.binaryContentId()));

      binaryContent.markAsFailed();
      binaryContentRepository.save(binaryContent);

      eventPublisher.publishEvent(
          new AsyncTaskFailureEvent(
              event.taskName(),
              event.requestId(),
              event.failureReason()
          )
      );

      eventPublisher.publishEvent(
          new AsyncFailedNotificationEvent(
              event.userId(),
              event.taskName(),
              event.requestId(),
              "파일 업로드 실패"
          )
      );

    } catch (Exception e) {
      log.error("업로드 실패 이벤트 처리 중 오류 발생: requestId={}", event.requestId(), e);
    }
  }

}
