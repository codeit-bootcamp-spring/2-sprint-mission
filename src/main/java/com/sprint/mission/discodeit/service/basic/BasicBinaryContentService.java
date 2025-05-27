package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.domain.BinaryContent;
import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.exception.binaryContent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;
  private final BinaryContentMapper binaryContentMapper;

  @Transactional
  @Override
  public BinaryContentDto createBinaryContent(BinaryContentCreateRequest request) {
    BinaryContent binaryContent = BinaryContent.create(request.fileName(),
        (long) request.bytes().length, request.contentType());
    BinaryContent createdBinaryContent = binaryContentRepository.save(binaryContent);
    binaryContentStorage.put(createdBinaryContent.getId(), request.bytes());

    return binaryContentMapper.toDto(createdBinaryContent);
  }

  @Transactional(readOnly = true)
  @Override
  public BinaryContentDto findById(UUID binaryContentId) {
    BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId)
        .orElseThrow(() -> BinaryContentNotFoundException.byId(binaryContentId));
    return binaryContentMapper.toDto(binaryContent);
  }

  @Transactional(readOnly = true)
  @Override
  public List<BinaryContentDto> findAllByIdIn(List<UUID> binaryContentIds) {
    return binaryContentRepository.findAllById(binaryContentIds).stream()
        .map(binaryContentMapper::toDto)
        .toList();
  }

  @Transactional
  @Override
  public void deleteBinaryContent(UUID binaryContentId) {
    if (!binaryContentRepository.existsById(binaryContentId)) {
      throw BinaryContentNotFoundException.byId(binaryContentId);
    }
    binaryContentRepository.deleteById(binaryContentId);
    binaryContentStorage.deleteById(binaryContentId);
  }

}
