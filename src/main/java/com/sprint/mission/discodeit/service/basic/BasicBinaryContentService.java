package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
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
    BinaryContent binaryContent = new BinaryContent(request.fileName(),
        (long) request.bytes().length, request.contentType(), request.bytes());
    binaryContentRepository.save(binaryContent);

    return binaryContent;
  }

  @Override
  public BinaryContent find(UUID binaryContentKey) {
    BinaryContent binaryContent = binaryContentRepository.findByKey(binaryContentKey);
    if (binaryContent == null) {
      throw new IllegalArgumentException("[Error] binaryContent is null");
    }

    return binaryContent;
  }

  @Override
  public List<BinaryContent> findAllByKeys(List<UUID> binaryContentKeys) {
    List<BinaryContent> binaryContents = binaryContentRepository.findAllByKeys(binaryContentKeys);
    if (binaryContents == null || binaryContents.isEmpty()) {
      throw new IllegalArgumentException("[Error] binaryContent is null");
    }

    return binaryContents;
  }

  @Override
  public void delete(UUID binaryContentKey) {
    BinaryContent binaryContent = binaryContentRepository.findByKey(binaryContentKey);
    if (binaryContent == null) {
      throw new IllegalArgumentException("[Error] binaryContent is null");
    }

    binaryContentRepository.delete(binaryContentKey);
  }
}
