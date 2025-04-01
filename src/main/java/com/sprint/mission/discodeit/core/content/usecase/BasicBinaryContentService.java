package com.sprint.mission.discodeit.core.content.usecase;

import com.sprint.mission.discodeit.adapter.inbound.content.dto.BinaryContentCreateRequestDTO;
import com.sprint.mission.discodeit.core.content.entity.BinaryContent;
import com.sprint.mission.discodeit.logging.CustomLogging;
import com.sprint.mission.discodeit.core.content.port.BinaryContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;

  @CustomLogging
  @Override
  public BinaryContent create(BinaryContentCreateRequestDTO binaryContentCreateRequestDTO) {
    BinaryContent binaryContent = new BinaryContent(binaryContentCreateRequestDTO.fileName(),
        binaryContentCreateRequestDTO.bytes().length, binaryContentCreateRequestDTO.contentType(),
        binaryContentCreateRequestDTO.bytes());
    return binaryContentRepository.save(binaryContent);
  }

  @Override
  public BinaryContent findById(UUID binaryId) {
    return binaryContentRepository.findById(binaryId);
  }

  @Override
  public List<BinaryContent> findAllByIdIn() {
    return binaryContentRepository.findAllByIdIn();
  }

  @CustomLogging
  @Override
  public void delete(UUID binaryId) {
    binaryContentRepository.delete(binaryId);
  }
}
