package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.util.FileUtil;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;

  @Override
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

    return binaryContentRepository.save(binaryContent);
  }

  @Override
  public BinaryContent findById(UUID id) {
    return binaryContentRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException(id + " 에 해당하는 BinaryContent를 찾을 수 없음"));
  }

  @Override
  public List<BinaryContent> findAllByIdIn(List<UUID> idList) {
    return idList.stream().map(this::findById).toList();
  }

  @Override
  public void delete(UUID id) {
    if (!binaryContentRepository.existsById(id)) {
      throw new NoSuchElementException(id + "에 해당하는 BinaryContent를 찾을 수 없음");
    }
    binaryContentRepository.deleteById(id);
  }

}
