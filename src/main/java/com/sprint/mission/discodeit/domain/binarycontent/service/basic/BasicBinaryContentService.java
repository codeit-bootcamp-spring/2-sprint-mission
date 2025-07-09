package com.sprint.mission.discodeit.domain.binarycontent.service.basic;

import com.sprint.mission.discodeit.domain.binarycontent.dto.BinaryContentRequest;
import com.sprint.mission.discodeit.domain.binarycontent.dto.BinaryContentResult;
import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.domain.binarycontent.exception.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.domain.binarycontent.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.domain.binarycontent.service.BinaryContentService;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentCore binaryContentCore;
  private final BinaryContentRepository binaryContentRepository;

  @Transactional
  @Override
  public BinaryContentResult createBinaryContent(BinaryContentRequest binaryContentRequest) {
    BinaryContent binaryContent = binaryContentCore.createBinaryContent(binaryContentRequest);

    return BinaryContentResult.fromEntity(binaryContent);
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
    binaryContentCore.delete(id);
  }

}
