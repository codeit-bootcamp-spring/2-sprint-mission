package com.sprint.mission.discodeit.storage.s3.event;

import com.sprint.mission.discodeit.entity.AsyncTaskFailure;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.BinaryContentUploadStatus;
import com.sprint.mission.discodeit.exception.binarycontent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.repository.AsyncTaskFailureRepository;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jboss.logging.MDC;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
@RequiredArgsConstructor
public class S3UploadEventListener {

  private final BinaryContentStorage binaryContentStorage;
  private final BinaryContentRepository binaryContentRepository;
  private final AsyncTaskFailureRepository asyncTaskFailureRepository;

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  // Service 메서드에 @Async를 직접 붙이는 것을 지양 -> EventListener에 붙이는 것이 좋다.
  // 메인 트랜잭션이 정상적으로 커밋된 이후에만 부가(비동기) 작업이 시작되므로, DB 일관성이 완벽하게 보장
  // Service 메서드 직접 @Async 사용시, 비즈니스와 인프라(비동기, 네트워크, 재시도, 예외처리 등) 코드가 뒤섞여 가독성과 테스트, 확장이 어려워짐
  @Async("asyncExecutor")
  @Retryable(
      maxAttempts = 3,
      recover = "createAsyncTaskFailure",
      backoff = @Backoff(delay = 1000, multiplier = 2)
  )
  public void handleS3UploadEvent(S3UploadEvent event) {
    // userService, messageService의 메서드와 다른 트랜잭션에서 실행됨 -> binaryContentStorage.put 메서드가 실패해도 메인 트랜잭션에 영향 X
    binaryContentStorage.put(event.id(), event.bytes());
    findBinaryContent(event.id()).updateStatus(BinaryContentUploadStatus.SUCCESS);
  }

  @Recover
  @Transactional
  public void createAsyncTaskFailure(Exception e, UUID binaryContentId) {
    AsyncTaskFailure asyncTaskFailure = AsyncTaskFailure.builder()
        .failureReason(e.getMessage())
        .requestId(MDC.get("requestId") != null ? MDC.get("requestId").toString() : "UNKNOWN")
        .taskName("S3FileUpload")
        .build();

    asyncTaskFailureRepository.save(asyncTaskFailure);
    findBinaryContent(binaryContentId).updateStatus(BinaryContentUploadStatus.FAILED);

    log.warn("S3 Upload retry finally failed - requestId: {}, binaryContentId: {}",
        MDC.get("requestId"), binaryContentId.toString());
  }

  private BinaryContent findBinaryContent(UUID binaryContentId) {
    return binaryContentRepository.findById(binaryContentId)
        .orElseThrow(() -> {
          log.warn(
              "BinaryContent find failed: binaryContent not found (method: {}, binaryContentId: {})",
              "S3Upload", binaryContentId);
          return new BinaryContentNotFoundException(Map.of("id", binaryContentId));
        });
  }
}
