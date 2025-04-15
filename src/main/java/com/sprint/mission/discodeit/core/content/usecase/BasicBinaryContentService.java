package com.sprint.mission.discodeit.core.content.usecase;

import com.sprint.mission.discodeit.adapter.outbound.content.LocalBinaryContentStorage;
import com.sprint.mission.discodeit.core.content.usecase.dto.BinaryContentResult;
import com.sprint.mission.discodeit.core.content.usecase.dto.CreateBinaryContentCommand;
import com.sprint.mission.discodeit.core.content.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.content.BinaryContentErrors;
import com.sprint.mission.discodeit.logging.CustomLogging;
import com.sprint.mission.discodeit.core.content.port.BinaryContentMetaRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentMetaRepositoryPort binaryContentMetaRepository;
  private final LocalBinaryContentStorage binaryContentStorage;

  @CustomLogging
  @Transactional
  @Override
  public BinaryContentResult create(CreateBinaryContentCommand command) {
    BinaryContent binaryContent = BinaryContent.create(command.fileName(),
        (long) command.bytes().length, command.contentType());
    binaryContentMetaRepository.save(binaryContent);
    binaryContentStorage.put(binaryContent.getId(), command.bytes());
    return BinaryContentResult.create(binaryContent);
  }

  @Override
  public BinaryContentResult findById(UUID binaryId) {
    BinaryContent binaryContent = binaryContentMetaRepository.findById(binaryId).orElseThrow(
        () -> BinaryContentErrors.binaryContentNotFoundError(binaryId)
    );
    return BinaryContentResult.create(binaryContent);
  }

  @Override
  public List<BinaryContentResult> findAllByIdIn(List<UUID> binaryContentIds) {
    return binaryContentMetaRepository.findAllByIdIn(binaryContentIds).stream().map(
        BinaryContentResult::create
    ).toList();
  }

  @CustomLogging
  @Override
  public void delete(UUID binaryId) {
    if (!binaryContentMetaRepository.existsId(binaryId)) {
      BinaryContentErrors.binaryContentNotFoundError(binaryId);
    }
    binaryContentMetaRepository.delete(binaryId);
  }
}
