package com.sprint.mission.discodeit.core.storage.service;

import com.sprint.mission.discodeit.core.storage.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.core.storage.entity.BinaryContent;
import com.sprint.mission.discodeit.core.storage.repository.BinaryContentStoragePort;
import com.sprint.mission.discodeit.core.storage.repository.JpaBinaryContentRepository;
import com.sprint.mission.discodeit.core.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicBinaryContentService implements BinaryContentService {

  private final JpaBinaryContentRepository binaryContentMetaRepository;
  private final BinaryContentStoragePort binaryContentStorage;

  @Override
  @Transactional
  public BinaryContent create(BinaryContentCreateRequest request) {
    if (request != null) {
      BinaryContent binaryContent = BinaryContent.create(request.fileName(),
          (long) request.bytes().length, request.contentType());

      binaryContentMetaRepository.save(binaryContent);
      binaryContentStorage.put(binaryContent.getId(), request.bytes());

      log.info("[BinaryContentService] Binary Content Created: {}", binaryContent.getId());
      return binaryContent;
    }
    log.warn("[BinaryContentService] Parameter is empty");
    return null;
  }

  @Override
  @Transactional(readOnly = true)
  public BinaryContent findById(UUID binaryId) {
    return binaryContentMetaRepository.findById(binaryId).orElseThrow(
        () -> new UserNotFoundException(ErrorCode.FILE_NOT_FOUND, binaryId)
    );
  }

  @Override
  @Transactional(readOnly = true)
  public List<BinaryContent> findAllByIdIn(List<UUID> binaryContentIds) {
    return binaryContentMetaRepository.findAllByIdIn((binaryContentIds));
  }

  @Override
  @Transactional
  public void delete(UUID binaryId) {
    BinaryContent binaryContent = binaryContentMetaRepository.findById(binaryId).orElseThrow(
        () -> new UserNotFoundException(ErrorCode.FILE_NOT_FOUND, binaryId)
    );
    binaryContentMetaRepository.delete(binaryContent);
    log.info("[BinaryContentService] Binary Content deleted successfully");
  }
}
