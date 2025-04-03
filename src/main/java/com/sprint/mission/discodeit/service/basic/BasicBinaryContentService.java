package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.application.dto.binarycontent.BinaryContentResult;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;

  public UUID createProfileImage(MultipartFile multipartFile) {
    if (multipartFile == null) {
      return null;
    }

    BinaryContent savedBinaryContent = binaryContentRepository.save(
        new BinaryContent(multipartFile));

    return savedBinaryContent.getId();
  }

  @Override
  public BinaryContentResult getById(UUID id) {
    BinaryContent binaryContent = binaryContentRepository.findByBinaryContentId(id)
        .orElseThrow(() -> new IllegalArgumentException("해당 ID를 가진 컨텐츠가 없습니다."));

    return BinaryContentResult.fromEntity(binaryContent);
  }

  @Override
  public List<BinaryContentResult> getByIdIn(List<UUID> ids) {
    return ids.stream()
        .map(this::getById)
        .toList();
  }

  @Override
  public void delete(UUID id) {
    binaryContentRepository.delete(id);
  }
}
