package com.sprint.mission.discodeit.domain.message.service.basic;

import com.sprint.mission.discodeit.common.event.event.S3AsyncFailedNotificationEvent;
import com.sprint.mission.discodeit.domain.binarycontent.dto.BinaryContentRequest;
import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContentUploadStatus;
import com.sprint.mission.discodeit.domain.binarycontent.exception.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.domain.binarycontent.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.domain.binarycontent.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.domain.user.dto.UserResult;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
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
public class MessageBinaryContentService {


  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;
  private final ApplicationEventPublisher eventPublisher;

  @Transactional
  public List<BinaryContent> createBinaryContents(List<BinaryContentRequest> contentRequests) {
    if (contentRequests == null) {
      return null;
    }
    if (contentRequests.isEmpty()) {
      return List.of();
    }

    Map<BinaryContentRequest, BinaryContent> binaryContents = saveBinaryContents(contentRequests);
    TransactionSynchronizationManager.registerSynchronization(
        new TransactionSynchronization() {
          @Override
          public void afterCommit() {
            List<CompletableFuture<UUID>> putResults = new ArrayList<>();

            for (Map.Entry<BinaryContentRequest, BinaryContent> binaryContentAndRequest : binaryContents.entrySet()) {
              BinaryContent content = binaryContentAndRequest.getValue();
              BinaryContentRequest request = binaryContentAndRequest.getKey();
              CompletableFuture<UUID> putResult = binaryContentStorage.put(content.getId(), request.bytes())
                  .whenComplete((result, exception) -> {
                    if (exception != null) {
                      log.error("S3 업로드 실패: binaryContentId = {}", content.getId(),
                          exception);
                      content.updateUploadStatus(BinaryContentUploadStatus.FAILED);
                      publishAsyncFailedEvent(content);
                    }
                    if (exception == null) {
                      content.updateUploadStatus(BinaryContentUploadStatus.SUCCESS);
                    }
                    log.debug("현재 쓰레드 {}", Thread.currentThread().getName());
                  });
              putResults.add(putResult);
            }

            CompletableFuture
                .allOf(putResults.toArray(new CompletableFuture[0]))
                .thenRun(() -> binaryContentRepository.saveAll(binaryContents.values()));
          }
        }
    );

    return binaryContents.values()
        .stream()
        .toList();
  }

  private Map<BinaryContentRequest, BinaryContent> saveBinaryContents(
      List<BinaryContentRequest> requests
  ) {
    Map<BinaryContentRequest, BinaryContent> binaryContents = new LinkedHashMap<>();
    for (BinaryContentRequest binaryContentRequest : requests) {
      BinaryContent binaryContent = new BinaryContent(
          binaryContentRequest.fileName(),
          binaryContentRequest.contentType(),
          binaryContentRequest.size());
      binaryContents.put(binaryContentRequest, binaryContent);
    }
    binaryContentRepository.saveAll(binaryContents.values());
    return binaryContents;
  }

  private void publishAsyncFailedEvent(BinaryContent binaryContent) {
    SecurityContext context = SecurityContextHolder.getContext();
    UserResult userResult = (UserResult) context.getAuthentication()
        .getPrincipal();

    S3AsyncFailedNotificationEvent s3AsyncFailedNotificationEvent = new S3AsyncFailedNotificationEvent(
        userResult.id(), binaryContent);
    eventPublisher.publishEvent(s3AsyncFailedNotificationEvent);
  }

  public void delete(UUID id) {
    validateBinaryContentExist(id);

    binaryContentRepository.deleteById(id);
  }

  private void validateBinaryContentExist(UUID id) {
    if (binaryContentRepository.existsById(id)) {
      return;
    }
    throw new BinaryContentNotFoundException(Map.of());
  }

}
