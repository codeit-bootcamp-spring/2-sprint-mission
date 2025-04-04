package com.sprint.mission.discodeit.core.content.usecase;

import com.sprint.mission.discodeit.core.content.usecase.dto.CreateBinaryContentCommand;
import com.sprint.mission.discodeit.core.content.entity.BinaryContent;
import com.sprint.mission.discodeit.logging.CustomLogging;
import com.sprint.mission.discodeit.core.content.port.BinaryContentRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentRepositoryPort binaryContentRepositoryPort;

  @CustomLogging
  @Override
  public BinaryContent create(CreateBinaryContentCommand command) {
    BinaryContent binaryContent = BinaryContent.create(command.fileName(),
        (long) command.bytes().length, command.contentType(),
        command.bytes());
    return binaryContentRepositoryPort.save(binaryContent);
  }

  @Override
  public BinaryContent findById(UUID binaryId) {
    return binaryContentRepositoryPort.findById(binaryId);
  }

  @Override
  public List<BinaryContent> findAllByIdIn(List<UUID> binaryContentIds) {
    return binaryContentRepositoryPort.findAllByIdIn(binaryContentIds);
  }

  @CustomLogging
  @Override
  public void delete(UUID binaryId) {
    binaryContentRepositoryPort.delete(binaryId);
  }
}
