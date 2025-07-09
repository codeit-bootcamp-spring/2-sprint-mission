package com.sprint.mission.discodeit.domain.binarycontent.service.basic;

import com.sprint.mission.discodeit.domain.binarycontent.dto.BinaryContentRequest;
import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.domain.binarycontent.exception.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.domain.binarycontent.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.domain.binarycontent.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class BinaryContentCore {

  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;

  @Transactional
  public BinaryContent createBinaryContent(BinaryContentRequest binaryContentRequest) {
    if (binaryContentRequest == null) {
      return null;
    }
    BinaryContent binaryContent = new BinaryContent(binaryContentRequest.fileName(),
        binaryContentRequest.contentType(), binaryContentRequest.size());
    BinaryContent savedBinaryContent = binaryContentRepository.save(binaryContent);
    binaryContentStorage.put(savedBinaryContent.getId(), binaryContentRequest.bytes());

    return savedBinaryContent;
  }

  @Transactional
  public List<BinaryContent> createBinaryContents(
      List<BinaryContentRequest> binaryContentRequests) {
    if (binaryContentRequests == null) {
      return null;
    }

    List<BinaryContent> binaryContents = new ArrayList<>();
    for (BinaryContentRequest binaryContentRequest : binaryContentRequests) {
      BinaryContent savedBinaryContent = binaryContentRepository.save(
          new BinaryContent(binaryContentRequest.fileName(), binaryContentRequest.contentType(),
              binaryContentRequest.size()));
      binaryContentStorage.put(savedBinaryContent.getId(), binaryContentRequest.bytes());
      binaryContents.add(savedBinaryContent);
    }

    return binaryContents;
  }

  public void delete(UUID id) {
    if (!binaryContentRepository.existsById(id)) {
      throw new BinaryContentNotFoundException(Map.of("userId", id));
    }

    binaryContentRepository.deleteById(id);
  }

}
