package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.service.binarycontent.CreateBinaryContentCommand;
import com.sprint.mission.discodeit.dto.service.binarycontent.CreateBinaryContentResult;
import com.sprint.mission.discodeit.dto.service.binarycontent.FindBinaryContentResult;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.RestExceptions;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentMapper binaryContentMapper;
  private Logger logger = LoggerFactory.getLogger(this.getClass());

  @Override
  @Transactional
  public CreateBinaryContentResult create(BinaryContent binaryContent) {
    BinaryContent createdBinaryContent = binaryContentRepository.save(binaryContent);

    return binaryContentMapper.toCreateBinaryContentResult(createdBinaryContent);
  }

  @Override
  @Transactional(readOnly = true)
  public FindBinaryContentResult find(UUID id) {
    BinaryContent binaryContent = findBinaryContentById(id);
    return binaryContentMapper.toFindBinaryContentResult(binaryContent);
  }

  @Override
  @Transactional(readOnly = true)
  // attachmentIds를 받아서 리스트를 조회
  public List<FindBinaryContentResult> findAllByIdIn(List<UUID> attachmentsId) {
    return attachmentsId.stream()
        .map(this::findBinaryContentById)
        .map(binaryContentMapper::toFindBinaryContentResult)
        .toList();
  }

  @Override
  public void delete(UUID id) {
    binaryContentRepository.deleteById(id);
  }

  private BinaryContent findBinaryContentById(UUID id) {
    return binaryContentRepository.findById(id)
        .orElseThrow(() -> {
          logger.error("binaryContent 찾기 실패: {}", id);
          return RestExceptions.BINARY_CONTENT_NOT_FOUND;
        });
  }

  private BinaryContent createBinaryContentEntity(
      CreateBinaryContentCommand createBinaryContentCommand) {
    return BinaryContent.builder()
        .filename(createBinaryContentCommand.filename())
        .size(createBinaryContentCommand.size())
        .contentType(createBinaryContentCommand.contentType())
        .bytes(createBinaryContentCommand.bytes())
        .build();
  }
}
