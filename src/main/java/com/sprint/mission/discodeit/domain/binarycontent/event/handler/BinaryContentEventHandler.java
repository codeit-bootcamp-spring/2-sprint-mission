package com.sprint.mission.discodeit.domain.binarycontent.event.handler;

import com.sprint.mission.discodeit.common.event.event.S3AsyncFailedEvent;
import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContentUploadStatus;
import com.sprint.mission.discodeit.domain.binarycontent.event.BinaryContentCreatedEvent;
import com.sprint.mission.discodeit.domain.binarycontent.exception.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.domain.binarycontent.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.domain.binarycontent.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.domain.user.dto.UserResult;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.PayloadApplicationEvent;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;


@Slf4j
@Component
@RequiredArgsConstructor
public class BinaryContentEventHandler {

  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;
  private final ApplicationEventPublisher eventPublisher;

  @TransactionalEventListener
  public void handle(
      PayloadApplicationEvent<List<BinaryContentCreatedEvent>> binaryContentsCreatedEvent
  ) {
    Map<UUID, BinaryContent> binaryContents = getBinaryContents(
        binaryContentsCreatedEvent.getPayload()
    );

    List<CompletableFuture<UUID>> uploadResults = new ArrayList<>();
    for (BinaryContentCreatedEvent binaryContentCreatedEvent : binaryContentsCreatedEvent.getPayload()) {
      UUID binaryContentId = binaryContentCreatedEvent.binaryContentId();
      BinaryContent binaryContent = binaryContents.get(binaryContentId);

      CompletableFuture<UUID> uploadResult = getBinaryStorageUploadResult(binaryContentId,
          binaryContentCreatedEvent, binaryContent);

      uploadResults.add(uploadResult);
    }

    CompletableFuture
        .allOf(uploadResults.toArray(new CompletableFuture[0]))
        .thenRun(() -> binaryContentRepository.saveAll(binaryContents.values()));
  }

  @TransactionalEventListener
  public void handle(BinaryContentCreatedEvent binaryContentCreatedEvent) {
    BinaryContent binaryContent = binaryContentRepository.findById(
            binaryContentCreatedEvent.binaryContentId())
        .orElseThrow(() -> new BinaryContentNotFoundException(Map.of()));

    CompletableFuture<UUID> uploadResult = getBinaryStorageUploadResult(
        binaryContent.getId(), binaryContentCreatedEvent, binaryContent);

    uploadResult.thenRun(() -> binaryContentRepository.save(binaryContent));
  }

  private CompletableFuture<UUID> getBinaryStorageUploadResult(UUID binaryContent,
      BinaryContentCreatedEvent binaryContentCreatedEvent, BinaryContent binaryContent1
  ) {
    return binaryContentStorage.put(binaryContent,
            binaryContentCreatedEvent.bytes())
        .whenComplete((result, exception) -> {
          if (exception != null) {
            binaryContent1.updateUploadStatus(BinaryContentUploadStatus.FAILED);
          }
          if (exception == null) {
            binaryContent1.updateUploadStatus(BinaryContentUploadStatus.SUCCESS);
          }
          publishS3UploadEvent(binaryContent1);
        });
  }

  private Map<UUID, BinaryContent> getBinaryContents(
      List<BinaryContentCreatedEvent> binaryContentsCreatedEvent
  ) {
    List<UUID> binaryContentIds = binaryContentsCreatedEvent.stream()
        .map(BinaryContentCreatedEvent::binaryContentId)
        .toList();
    return binaryContentRepository.findAllById(binaryContentIds)
        .stream()
        .collect(Collectors.toMap(
            BinaryContent::getId,
            Function.identity()
        ));
  }

  private void publishS3UploadEvent(BinaryContent binaryContent) {
    SecurityContext context = SecurityContextHolder.getContext();
    UserResult userResult = (UserResult) context.getAuthentication()
        .getPrincipal();

    S3AsyncFailedEvent s3AsyncFailedEvent = new S3AsyncFailedEvent(userResult.id(), binaryContent);
    eventPublisher.publishEvent(s3AsyncFailedEvent);
  }

}
