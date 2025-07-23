package com.sprint.mission.discodeit.domain.user.service.basic;

import com.sprint.mission.discodeit.domain.binarycontent.dto.BinaryContentRequest;
import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.domain.binarycontent.exception.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.domain.binarycontent.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.domain.binarycontent.storage.BinaryContentStorage;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
@RequiredArgsConstructor
public class UserBinaryContentService {

  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;

  @Transactional
  public BinaryContent createBinaryContent(BinaryContentRequest binaryContentRequest) {
    if (binaryContentRequest == null) {
      return null;
    }
    BinaryContent binaryContent = new BinaryContent(binaryContentRequest.fileName(),
        binaryContentRequest.contentType(), binaryContentRequest.size());
    BinaryContent savedBinaryContent = binaryContentRepository.save(binaryContent);

    TransactionSynchronizationManager.registerSynchronization(
        new TransactionSynchronization() {
          @Override
          public void afterCommit() {
            binaryContentStorage.put(savedBinaryContent.getId(), binaryContentRequest.bytes());
          }
        }
    );

    return savedBinaryContent;
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
