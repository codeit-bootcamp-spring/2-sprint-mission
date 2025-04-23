package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.domain.BinaryContent;
import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
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
    BinaryContent createdBinaryContent = binaryContentRepository.save(
        request.convertCreateRequestToBinaryContent());
    binaryContentStorage.put(createdBinaryContent.getId(), request.bytes());

    return binaryContentMapper.toDto(createdBinaryContent);
  }

  @Transactional(readOnly = true)
  @Override
  public BinaryContentDto findById(UUID binaryContentId) {
    BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId)
        .orElseThrow(() -> new NoSuchElementException(
            "해당 ID의 BinaryContent를 찾을 수 없습니다: " + binaryContentId));
    return binaryContentMapper.toDto(binaryContent);
  }

  @Transactional(readOnly = true)
  @Override
  public List<BinaryContentDto> findAllByIdIn(List<UUID> binaryContentIds) {
    List<BinaryContent> binaryContents = binaryContentRepository.findAllByIdIn(binaryContentIds);
    List<BinaryContentDto> binaryContentDtos = new ArrayList<>();
    binaryContents.forEach(
        binaryContent -> binaryContentDtos.add(binaryContentMapper.toDto(binaryContent)));
    return binaryContentDtos;
  }

  @Transactional
  @Override
  public void deleteBinaryContent(UUID binaryContentId) {
    checkBinaryContentExists(binaryContentId);
    binaryContentRepository.deleteById(binaryContentId);
    binaryContentStorage.deleteById(binaryContentId);
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
