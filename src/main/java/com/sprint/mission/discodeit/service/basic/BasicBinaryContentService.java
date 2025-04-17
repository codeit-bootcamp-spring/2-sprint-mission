package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.dto.service.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.service.binarycontent.BinaryContentDto;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.util.FileUtil;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;

  @Override
  @Transactional
  public BinaryContent create(BinaryContentCreateRequest request) {
    String originalFilename = request.file().getOriginalFilename();
    String contentType = request.file().getContentType();
    long size = request.file().getSize();
    byte[] bytes;
    try {
      bytes = request.file().getBytes();
    } catch (IOException e) {
      throw new RuntimeException("파일 변환 중 오류 발생: " + e);
    }
    if (originalFilename != null && !FileUtil.isAllowedExtension(originalFilename)) {
      throw new IllegalArgumentException("허용하지 않는 파일");
    }
    BinaryContent binaryContent = new BinaryContent(originalFilename, contentType, size, bytes);

    binaryContentRepository.save(binaryContent);
    binaryContentStorage.put(binaryContent.getId(), binaryContent.getBytes());
    return binaryContent;
  }

  @Override
  public BinaryContentDto findById(UUID id) {
    BinaryContent binaryContent = binaryContentRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException(id + " 에 해당하는 BinaryContent를 찾을 수 없음"));
    return BinaryContentDto.of(binaryContent);
  }

  @Override
  public List<BinaryContentDto> findAllByIdIn(List<UUID> idList) {
    return idList.stream().map(this::findById).toList();
  }

  @Override
  public BinaryContentDto findContentById(UUID id) {
    BinaryContent content = binaryContentRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException(id + " 에 해당하는 BinaryContent를 찾을 수 없음"));
    return BinaryContentDto.of(content);
  }

  @Override
  public void delete(UUID id) {
    if (!binaryContentRepository.existsById(id)) {
      throw new NoSuchElementException(id + "에 해당하는 BinaryContent를 찾을 수 없음");
    }
    binaryContentRepository.deleteById(id);
  }
}
