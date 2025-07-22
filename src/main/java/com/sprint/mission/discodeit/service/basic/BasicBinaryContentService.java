package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.service.binarycontent.CreateBinaryContentResult;
import com.sprint.mission.discodeit.dto.service.binarycontent.FindBinaryContentResult;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.binarycontent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentMapper binaryContentMapper;

  @Override
  @Transactional
  public CreateBinaryContentResult create(BinaryContent binaryContent) {
    BinaryContent createdBinaryContent = binaryContentRepository.save(binaryContent);

    return binaryContentMapper.toCreateBinaryContentResult(createdBinaryContent);
  }

  @Override
  @Transactional(readOnly = true)
  public FindBinaryContentResult find(UUID id) {
    BinaryContent binaryContent = findBinaryContentById(id, "find");
    return binaryContentMapper.toFindBinaryContentResult(binaryContent);
  }

  @Override
  @Transactional(readOnly = true)
  // attachmentIds를 받아서 리스트를 조회
  public List<FindBinaryContentResult> findAllByIdIn(List<UUID> attachmentsId) {
    return binaryContentRepository.findAllByIdIn(attachmentsId).stream()
        .map(binaryContentMapper::toFindBinaryContentResult)
        .toList();
  }

  @Override
  public void delete(UUID id) {
    findBinaryContentById(id, "delete");
    binaryContentRepository.deleteById(id);
  }

  private BinaryContent findBinaryContentById(UUID id, String method) {
    return binaryContentRepository.findById(id)
        .orElseThrow(() -> {
          log.warn("BinaryContent {} failed: binaryContent not found (binaryContentId: {})", method,
              id);
          return new BinaryContentNotFoundException(
              Map.of("binaryContentId", id, "method", method));
        });
  }
}
