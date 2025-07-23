package com.sprint.mission.discodeit.domain.binarycontent.service.basic;

import com.sprint.mission.discodeit.domain.binarycontent.dto.BinaryContentRequest;
import com.sprint.mission.discodeit.domain.binarycontent.dto.BinaryContentResult;
import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.domain.binarycontent.exception.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.domain.binarycontent.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.domain.binarycontent.service.BinaryContentService;
import com.sprint.mission.discodeit.domain.binarycontent.storage.BinaryContentStorage;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;


@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentStorage binaryContentStorage;
  private final BinaryContentRepository binaryContentRepository;

  @Transactional
  @Override
  public BinaryContentResult createBinaryContent(BinaryContentRequest binaryContentRequest) {
    if (binaryContentRequest == null) {
      return null;
    }
    BinaryContent binaryContent = new BinaryContent(binaryContentRequest.fileName(),
        binaryContentRequest.contentType(), binaryContentRequest.size());
    BinaryContent savedBinaryContent = binaryContentRepository.save(binaryContent);

    binaryContentStorage.put(savedBinaryContent.getId(), binaryContentRequest.bytes());

    return BinaryContentResult.fromEntity(savedBinaryContent);
  }

  @Transactional(readOnly = true)
  @Override
  public BinaryContentResult getById(UUID id) {
    BinaryContent binaryContent = binaryContentRepository.findById(id)
        .orElseThrow(() -> new BinaryContentNotFoundException(Map.of()));

    return BinaryContentResult.fromEntity(binaryContent);
  }

  @Transactional(readOnly = true)
  @Override
  public List<BinaryContentResult> getByIdIn(List<UUID> ids) {
    return binaryContentRepository.findAllById(ids)
        .stream()
        .map(BinaryContentResult::fromEntity)
        .toList();
  }

  @Override
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
