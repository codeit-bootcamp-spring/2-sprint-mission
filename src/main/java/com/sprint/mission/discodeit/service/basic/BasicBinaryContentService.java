package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import jakarta.transaction.Transactional;
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

  @Transactional
  @Override
  public BinaryContent createBinaryContent(MultipartFile profile) {
    try {
      BinaryContent binaryContent = BinaryContent.builder()
          .fileName(profile.getOriginalFilename())
          .size(profile.getSize())
          .contentType(profile.getContentType())
          .bytes(profile.getBytes())
          .build();
      binaryContentRepository.save(binaryContent);

      return binaryContent;
    } catch (IOException e) {
      throw new RuntimeException("프로필 이미지 업로드 실패", e);
    }
  }

  @Override
  public BinaryContent findBinaryContent(UUID binaryContentId) {
    return binaryContentRepository.findById(binaryContentId)
        .orElseThrow(() -> new NoSuchElementException(
            "BinaryContentId: " + binaryContentId + " not found"));
  }

  @Override
  public List<BinaryContent> findAllBinaryContent() {
    return binaryContentRepository.findAll();
  }

  @Override
  public void deleteBinaryContent(UUID binaryContentId) {
    binaryContentRepository.deleteById(binaryContentId);
  }
}
