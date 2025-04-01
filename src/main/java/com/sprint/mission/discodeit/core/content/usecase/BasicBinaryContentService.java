package com.sprint.mission.discodeit.core.content.usecase;

import com.sprint.mission.discodeit.adapter.inbound.content.dto.BinaryContentCreateRequestDTO;
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
  public BinaryContent create(BinaryContentCreateRequestDTO binaryContentCreateRequestDTO) {
    BinaryContent binaryContent = BinaryContent.create(binaryContentCreateRequestDTO.fileName(),
        binaryContentCreateRequestDTO.bytes().length, binaryContentCreateRequestDTO.contentType(),
        binaryContentCreateRequestDTO.bytes());
    return binaryContentRepositoryPort.save(binaryContent);
  }

  @Override
  public BinaryContent findById(UUID binaryId) {
    return binaryContentRepositoryPort.findById(binaryId);
  }

  @Override
  public List<BinaryContent> findAllByIdIn() {
    return binaryContentRepositoryPort.findAllByIdIn();
  }

  @CustomLogging
  @Override
  public void delete(UUID binaryId) {
    binaryContentRepositoryPort.delete(binaryId);
  }
}
