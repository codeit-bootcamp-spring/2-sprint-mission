package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
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
  private final BinaryContentStorage binaryContentStorage;
  private final BinaryContentMapper binaryContentMapper;

  @Transactional
  @Override
  public BinaryContent createBinaryContent(MultipartFile profile) {
    try {
      BinaryContent binaryContent = BinaryContent.builder()
          .fileName(profile.getOriginalFilename())
          .size(profile.getSize())
          .contentType(profile.getContentType())
          .build();
      binaryContentRepository.save(binaryContent);
      binaryContentStorage.put(binaryContent.getId(), profile.getBytes());

      return binaryContent;
    } catch (IOException e) {
      throw new RuntimeException("프로필 이미지 업로드 실패", e);
    }
  }

  @Override
  public BinaryContentDto findBinaryContent(UUID binaryContentId) {
    BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId)
        .orElseThrow(() -> new NoSuchElementException(
            "BinaryContentId: " + binaryContentId + " not found"));
    return binaryContentMapper.toDto(binaryContent);
  }

  @Override
  public List<BinaryContentDto> findAllByIdIn(List<UUID> binaryContentIds) {
    return binaryContentRepository.findAllByIdIn(binaryContentIds).stream()
        .map(binaryContentMapper::toDto)
        .toList();
  }

  @Override
  public List<BinaryContentDto> findAllBinaryContent() {
    return binaryContentRepository.findAll().stream()
        .map(binaryContentMapper::toDto)
        .toList();
  }

  @Override
  public void deleteBinaryContent(UUID binaryContentId) {
    binaryContentRepository.deleteById(binaryContentId);
  }
}
