package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;

  @Override
  public BinaryContent create(BinaryContentCreateRequest request) {
    String fileName = request.fileName();
    byte[] bytes = request.bytes();
    String contentType = request.contentType();
    BinaryContent binaryContent = new BinaryContent(
        fileName,
        (long) bytes.length,
        contentType,
        bytes
    );
    return binaryContentRepository.save(binaryContent);
  }

  @Transactional(readOnly = true)
  @Override
  public BinaryContent find(UUID binaryContentId) {
    return binaryContentRepository.findById(binaryContentId)
        .orElseThrow(() -> new NoSuchElementException(
            "BinaryContent with id " + binaryContentId + " not found")
        );
  }

  @Transactional(readOnly = true)
  @Override
  public List<BinaryContent> findAllByIdIn(List<UUID> binaryContentIds) {
    return binaryContentRepository.findAllByIdIn(binaryContentIds);
  }

  @Override
  public void delete(UUID binaryContentId) {
    BinaryContent binaryContent = this.find(binaryContentId);

    binaryContentRepository.delete(binaryContent);
  }
}
