package com.sprint.mission.discodeit.core.content.repository;

import com.sprint.mission.discodeit.core.content.entity.BinaryContent;
import com.sprint.mission.discodeit.core.content.port.BinaryContentMetaRepositoryPort;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BinaryContentMetaRepositoryAdapter implements BinaryContentMetaRepositoryPort {

  private final JpaBinaryContentRepository jpaBinaryContentRepository;

  @Override
  public BinaryContent save(BinaryContent binaryContent) {
    return jpaBinaryContentRepository.save(binaryContent);
  }

  @Override
  public Optional<BinaryContent> findById(UUID binaryId) {
    return jpaBinaryContentRepository.findById(binaryId);
  }

  @Override
  public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
    return jpaBinaryContentRepository.findAllById(ids);
  }

  @Override
  public boolean existsId(UUID binaryId) {
    return jpaBinaryContentRepository.existsById(binaryId);
  }

  @Override
  public void delete(UUID binaryId) {
    jpaBinaryContentRepository.deleteById(binaryId);
  }
}
