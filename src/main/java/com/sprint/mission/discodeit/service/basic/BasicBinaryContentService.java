package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class BasicBinaryContentService implements BinaryContentService {

  BinaryContentRepository binaryContentRepository;

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

  @Override
  public BinaryContent findById(UUID binaryContentId) {
    return binaryContentRepository.findById(binaryContentId);
  }

  public List<BinaryContent> findAllByIds(List<UUID> binaryContentIds) {
    return binaryContentRepository.findAllByIds(binaryContentIds);
  }

  @Override
  public List<BinaryContent> findAll() {
    return binaryContentRepository.findAll();
  }

  @Override
  public void delete(UUID binaryContentId) {
    binaryContentRepository.delete(binaryContentId);
  }
}
