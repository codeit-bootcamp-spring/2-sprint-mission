package com.sprint.mission.discodeit.domain.message.service.basic;

import com.sprint.mission.discodeit.domain.binarycontent.dto.BinaryContentRequest;
import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContentUploadStatus;
import com.sprint.mission.discodeit.domain.binarycontent.event.BinaryContentCreatedEvent;
import com.sprint.mission.discodeit.domain.binarycontent.exception.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.domain.binarycontent.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.domain.binarycontent.storage.BinaryContentStorage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageBinaryContentService {

  private final BinaryContentRepository binaryContentRepository;
  private final ApplicationEventPublisher eventPublisher;

  @Transactional
  public List<BinaryContent> createBinaryContents(List<BinaryContentRequest> contentRequests) {
    if (contentRequests == null || contentRequests.isEmpty()) {
      return null;
    }

    Map<BinaryContent, BinaryContentRequest> binaryContents = saveBinaryContents(contentRequests);

    publishBinaryContentsCreatedEvent(binaryContents);
    return binaryContents.keySet()
        .stream()
        .toList();
  }

  @Transactional
  public void delete(UUID id) {
    validateBinaryContentExist(id);

    binaryContentRepository.deleteById(id);
  }

  private void publishBinaryContentsCreatedEvent(
      Map<BinaryContent, BinaryContentRequest> binaryContents
  ) {
    List<BinaryContentCreatedEvent> binaryContentsCreatedEvent = BinaryContentCreatedEvent.createBinaryContentsCreatedEvent(
        binaryContents);
    eventPublisher.publishEvent(binaryContentsCreatedEvent);
  }

  private void validateBinaryContentExist(UUID id) {
    if (binaryContentRepository.existsById(id)) {
      return;
    }
    throw new BinaryContentNotFoundException(Map.of());
  }

  private Map<BinaryContent, BinaryContentRequest> saveBinaryContents(
      List<BinaryContentRequest> requests
  ) {
    Map<BinaryContent, BinaryContentRequest> binaryContents = new LinkedHashMap<>();
    for (BinaryContentRequest binaryContentRequest : requests) {
      BinaryContent binaryContent = new BinaryContent(
          binaryContentRequest.fileName(),
          binaryContentRequest.contentType(),
          binaryContentRequest.size());
      binaryContents.put(binaryContent, binaryContentRequest);
    }
    binaryContentRepository.saveAll(binaryContents.keySet());
    return binaryContents;
  }

}
