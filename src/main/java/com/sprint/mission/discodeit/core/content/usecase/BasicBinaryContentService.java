package com.sprint.mission.discodeit.core.content.usecase;

import com.sprint.mission.discodeit.core.content.entity.BinaryContent;
import com.sprint.mission.discodeit.core.content.repository.JpaBinaryContentRepository;
import com.sprint.mission.discodeit.core.content.repository.LocalBinaryContentStorage;
import com.sprint.mission.discodeit.core.content.usecase.dto.CreateBinaryContentCommand;
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
  private final LocalBinaryContentStorage binaryContentStorage;

  @Override
  @Transactional
  public BinaryContent create(CreateBinaryContentCommand command) {
    if (command != null) {
      BinaryContent binaryContent = BinaryContent.create(command.fileName(),
          (long) command.bytes().length, command.contentType());
      binaryContentMetaRepository.save(binaryContent);
      binaryContentStorage.put(binaryContent.getId(), command.bytes());
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
    if (!binaryContentMetaRepository.existsById(binaryId)) {
      log.warn("[BinaryContentService] To Delete Binary Content is failed");
      throw new UserNotFoundException(ErrorCode.FILE_NOT_FOUND, binaryId);
    }
    binaryContentMetaRepository.deleteById(binaryId);
    log.info("[BinaryContentService] Binary Content deleted successfully");
  }
}
