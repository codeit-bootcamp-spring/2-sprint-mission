package com.sprint.mission.discodeit.domain.user.service.basic;

import com.sprint.mission.discodeit.common.event.event.S3AsyncFailedNotificationEvent;
import com.sprint.mission.discodeit.domain.binarycontent.dto.BinaryContentRequest;
import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContentUploadStatus;
import com.sprint.mission.discodeit.domain.binarycontent.exception.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.domain.binarycontent.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.domain.binarycontent.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.domain.user.dto.UserResult;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserBinaryContentService {

  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;
  private final ApplicationEventPublisher eventPublisher;

  @Transactional
  public BinaryContent createBinaryContent(BinaryContentRequest binaryContentRequest) {
    if (binaryContentRequest == null) {
      return null;
    }
    BinaryContent binaryContent = new BinaryContent(binaryContentRequest.fileName(),
        binaryContentRequest.contentType(), binaryContentRequest.size());
    BinaryContent savedBinaryContent = binaryContentRepository.save(binaryContent);

    // 로직이 너무 길다. 추후에 이벤트로 수정 바람
    TransactionSynchronizationManager.registerSynchronization(
        new TransactionSynchronization() {
          @Override
          public void afterCommit() {
            binaryContentStorage.put(savedBinaryContent.getId(), binaryContentRequest.bytes())
                .whenComplete((result, exception) -> {
                  if (exception != null) {
                    savedBinaryContent.updateUploadStatus(BinaryContentUploadStatus.FAILED);
                    publishAsyncFailedEvent(savedBinaryContent);
                  }
                  if (exception == null) {
                    savedBinaryContent.updateUploadStatus(BinaryContentUploadStatus.SUCCESS);
                  }
                  binaryContentRepository.save(binaryContent);
                });
          }
        }
    );

    return savedBinaryContent;
  }

  private void publishAsyncFailedEvent(BinaryContent binaryContent) {
    SecurityContext context = SecurityContextHolder.getContext();
    UserResult userResult = (UserResult) context.getAuthentication()
        .getPrincipal();

    S3AsyncFailedNotificationEvent s3AsyncFailedNotificationEvent = new S3AsyncFailedNotificationEvent(
        userResult.id(), binaryContent);
    eventPublisher.publishEvent(s3AsyncFailedNotificationEvent);
  }

  public void delete(BinaryContent binaryContent) {
    if (binaryContent == null) {
      return;
    }
    validateBinaryContentExist(binaryContent.getId());

    binaryContentRepository.deleteById(binaryContent.getId());
  }

  private void validateBinaryContentExist(UUID id) {
    if (binaryContentRepository.existsById(id)) {
      return;
    }
    throw new BinaryContentNotFoundException(Map.of());
  }

}
