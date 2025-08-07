package com.sprint.mission.discodeit.domain.user.service.basic;

import com.sprint.mission.discodeit.domain.binarycontent.dto.BinaryContentRequest;
import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.domain.binarycontent.event.BinaryContentCreatedEvent;
import com.sprint.mission.discodeit.domain.binarycontent.exception.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.domain.binarycontent.repository.BinaryContentRepository;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserBinaryContentService {

  private final BinaryContentRepository binaryContentRepository;
  private final ApplicationEventPublisher eventPublisher;

  @Transactional
  public BinaryContent createBinaryContent(BinaryContentRequest binaryContentRequest) {
    if (binaryContentRequest == null) {
      return null;
    }
    BinaryContent binaryContent = new BinaryContent(binaryContentRequest.fileName(),
        binaryContentRequest.contentType(), binaryContentRequest.size());
    BinaryContent savedBinaryContent = binaryContentRepository.save(binaryContent);

    publishBinaryContentCreatedEvent(binaryContentRequest, savedBinaryContent);

    return savedBinaryContent;
  }

  private void publishBinaryContentCreatedEvent(BinaryContentRequest binaryContentRequest,
      BinaryContent savedBinaryContent) {
    BinaryContentCreatedEvent binaryContentCreatedEvent = BinaryContentCreatedEvent.createBinaryContentCreatedEvent(
        savedBinaryContent, binaryContentRequest);
    eventPublisher.publishEvent(binaryContentCreatedEvent);
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
