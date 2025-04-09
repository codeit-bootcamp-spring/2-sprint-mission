package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;

  @Override
  public BinaryContent createBinaryContent(BinaryContentCreateRequest request) {
    return binaryContentRepository.save(request.convertCreateRequestToBinaryContent());
  }

  @Override
  public BinaryContent findById(UUID binaryContentId) {
    return binaryContentRepository.findById(binaryContentId)
        .orElseThrow(() -> new NoSuchElementException(
            "해당 ID의 BinaryContent를 찾을 수 없습니다: " + binaryContentId));
  }

  @Override
  public List<BinaryContent> findAllByIdIn(List<UUID> binaryContentIds) {
    return binaryContentRepository.findAllByIdIn(binaryContentIds);
  }

  @Override
  public void deleteBinaryContent(UUID binaryContentId) {
    checkBinaryContentExists(binaryContentId);
    binaryContentRepository.deleteById(binaryContentId);
  }

  /*******************************
   * Validation check
   *******************************/
  private void checkBinaryContentExists(UUID binaryContentId) {
    if (binaryContentRepository.findById(binaryContentId).isEmpty()) {
      throw new NoSuchElementException("해당 BinaryContent가 존재하지 않습니다. : " + binaryContentId);
    }
  }

}
