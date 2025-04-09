package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;

  @Override
  public UUID createBinaryContent(MultipartFile profile) {
    try {
      BinaryContent binaryContent = new BinaryContent(
          profile.getOriginalFilename(),
          profile.getSize(),
          profile.getContentType(),
          profile.getBytes() // getBytes()가 IOException을 던질 수 있음
      );
      binaryContentRepository.addBinaryContent(binaryContent);

      return binaryContent.getId();
    } catch (IOException e) {
      throw new RuntimeException("프로필 이미지 업로드 실패", e);
    }
  }

  @Override
  public BinaryContent findBinaryContent(UUID binaryContentId) {
    return binaryContentRepository.findBinaryContentById(binaryContentId)
        .orElseThrow(() -> new NoSuchElementException(
            "BinaryContent with id: " + binaryContentId + "not found"));
  }

  @Override
  public List<BinaryContent> findAllBinaryContent() {
    return binaryContentRepository.findAllBinaryContents();
  }

  @Override
  public void deleteBinaryContent(UUID binaryContentId) {
    binaryContentRepository.deleteBinaryContentById(binaryContentId);
  }
}
