package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.controller.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity._BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;

  @Override
  public _BinaryContent create(BinaryContentCreateRequest request) {
    String fileName = request.fileName();
    byte[] bytes = request.bytes();
    String contentType = request.contentType();
    _BinaryContent binaryContent = new _BinaryContent(
        fileName,
        (long) bytes.length,
        contentType,
        bytes
    );
    return binaryContentRepository.save(binaryContent);
  }

  @Override
  public _BinaryContent find(UUID binaryContentId) {
    return binaryContentRepository.findById(binaryContentId)
        .orElseThrow(() -> new NoSuchElementException(
            "BinaryContent with id " + binaryContentId + " not found"));
  }

  @Override
  public List<_BinaryContent> findAllByIdIn(List<UUID> binaryContentIds) {
    return binaryContentRepository.findAllByIdIn(binaryContentIds).stream()
        .toList();
  }

  @Override
  public void delete(UUID binaryContentId) {
    if (!binaryContentRepository.existsById(binaryContentId)) {
      throw new NoSuchElementException("BinaryContent with id " + binaryContentId + " not found");
    }
    binaryContentRepository.deleteById(binaryContentId);
  }
}
