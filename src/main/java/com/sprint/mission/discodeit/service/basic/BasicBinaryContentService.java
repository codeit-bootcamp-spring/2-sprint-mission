package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.common.BinaryContent;
import com.sprint.mission.discodeit.exception.ResourceNotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;

  @Override
  public BinaryContent create(BinaryContentCreateRequest request) {
    BinaryContent binaryContent = new BinaryContent(
        request.fileName(),
        (long) request.bytes().length,
        request.contentType(),
        request.bytes());
    return binaryContentRepository.save(binaryContent);
  }

  @Override
  public BinaryContent find(UUID binaryContentId) {
    return binaryContentRepository.findById(binaryContentId)
        .orElseThrow(() -> new ResourceNotFoundException("해당 BinaryContent 없음"));
  }

  @Override
  public List<BinaryContent> findAllByIdIn(List<UUID> binaryContentIds) {
    return binaryContentRepository.findAllByIdIn(binaryContentIds);
  }

  @Override
  public void delete(UUID binaryContentId) {
    if (!binaryContentRepository.existsById(binaryContentId)) {
      throw new ResourceNotFoundException("해당 BinaryContent 없음");
    }
    binaryContentRepository.deleteById(binaryContentId);
  }
}
