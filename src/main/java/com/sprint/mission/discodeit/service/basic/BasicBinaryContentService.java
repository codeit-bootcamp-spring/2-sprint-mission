package com.sprint.mission.discodeit.service.basic;

import static com.sprint.mission.discodeit.util.FileUtils.getBytesFromMultiPartFile;

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

  @Override
  public BinaryContentResult createProfileImage(MultipartFile multipartFile) {
    if (multipartFile == null) {
      return null;
    }

    BinaryContent binaryContent = new BinaryContent(multipartFile.getName(),
        multipartFile.getContentType(),
        multipartFile.getSize(), getBytesFromMultiPartFile(multipartFile));

    BinaryContent savedBinaryContent = binaryContentRepository.save(binaryContent);

    return BinaryContentResult.fromEntity(savedBinaryContent);
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
