package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.InputStream;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentMapper binaryContentMapper;
  private final BinaryContentStorage binaryContentStorage;

  @Override
  public BinaryContentDto findById(UUID binaryContentId) {
    BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId)
        .orElseThrow(
            () -> new NoSuchElementException(binaryContentId + "에 해당하는 파일을 찾는데 실패하였습니다."));
    InputStream is = binaryContentStorage.get(binaryContent.getId());
    return binaryContentMapper.toDto(binaryContent, is);
  }

  @Override
  public List<BinaryContentDto> findByIdIn(List<UUID> binaryContentIdList) {
    return binaryContentRepository.findAll().stream()
        .filter(binaryContent -> binaryContentIdList.contains(binaryContent.getId()))
        .map(binaryContent -> findById(binaryContent.getId()))
        .toList();
  }
}
