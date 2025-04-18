package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.common.BinaryContent;
import com.sprint.mission.discodeit.exception.ResourceNotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;

  @Override
  public BinaryContent create(BinaryContentCreateRequest request) {
    BinaryContent binaryContent = new BinaryContent(
        request.fileName(),
        (long) request.bytes().length,
        request.contentType());

    binaryContentRepository.save(binaryContent);

    binaryContentStorage.put(binaryContent.getId(), request.bytes());
    return binaryContent;
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
  public void delete(BinaryContent binaryContent) {
    BinaryContent found = binaryContentRepository.findById(binaryContent.getId())
        .orElseThrow(() -> new ResourceNotFoundException("해당 BinaryContent 없음"));

    binaryContentStorage.delete(found.getId());
    binaryContentRepository.delete(found);
  }
}
