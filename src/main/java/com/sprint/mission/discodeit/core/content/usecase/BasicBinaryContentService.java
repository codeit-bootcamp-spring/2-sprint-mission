package com.sprint.mission.discodeit.core.content.usecase;

import com.sprint.mission.discodeit.adapter.outbound.content.LocalBinaryContentStorage;
import com.sprint.mission.discodeit.core.content.entity.BinaryContent;
import com.sprint.mission.discodeit.core.content.port.BinaryContentMetaRepositoryPort;
import com.sprint.mission.discodeit.core.content.usecase.dto.CreateBinaryContentCommand;
import com.sprint.mission.discodeit.exception.content.BinaryContentErrors;
import com.sprint.mission.discodeit.logging.CustomLogging;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentMetaRepositoryPort binaryContentMetaRepository;
  private final LocalBinaryContentStorage binaryContentStorage;

  @CustomLogging
  @Override
  @Transactional
  public BinaryContent create(CreateBinaryContentCommand command) {
    BinaryContent binaryContent = BinaryContent.create(command.fileName(),
        (long) command.bytes().length, command.contentType());
    binaryContentMetaRepository.save(binaryContent);
    binaryContentStorage.put(binaryContent.getId(), command.bytes());
    return binaryContent;
  }

  @Override
  @Transactional(readOnly = true)
  public BinaryContent findById(UUID binaryId) {
    return binaryContentMetaRepository.findById(binaryId).orElseThrow(
        () -> BinaryContentErrors.binaryContentNotFoundError(binaryId)
    );
  }

  @Override
  @Transactional(readOnly = true)
  public List<BinaryContent> findAllByIdIn(List<UUID> binaryContentIds) {
    return binaryContentMetaRepository.findAllByIdIn(binaryContentIds);
  }

  @CustomLogging
  @Override
  @Transactional
  public void delete(UUID binaryId) {
    if (!binaryContentMetaRepository.existsId(binaryId)) {
      BinaryContentErrors.binaryContentNotFoundError(binaryId);
    }
    binaryContentMetaRepository.delete(binaryId);
  }
}
