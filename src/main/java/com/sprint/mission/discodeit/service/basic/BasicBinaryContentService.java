package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.service.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.service.binarycontent.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.binarycontent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.exception.binarycontent.UnsupportedFileTypeException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.util.FileUtil;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;
  private final BinaryContentMapper binaryContentMapper;

  @Override
  @Transactional
  public BinaryContent create(BinaryContentCreateRequest request) {
    log.debug("파일 생성 시작: {}", request);
    String originalFilename = request.file().getOriginalFilename();
    String contentType = request.file().getContentType();
    long size = request.file().getSize();
    byte[] bytes;
    try {
      bytes = request.file().getBytes();
    } catch (IOException e) {
      log.error("파일 변환 도중 예외 발생", e);
      throw new RuntimeException(e);
    }
    if (originalFilename != null && !FileUtil.isAllowedExtension(originalFilename)) {
      throw new UnsupportedFileTypeException().isNotAllowedFile(originalFilename);
    }
    BinaryContent binaryContent = new BinaryContent(originalFilename, contentType, size);

    binaryContentRepository.save(binaryContent);
    binaryContentStorage.put(binaryContent.getId(), bytes);
    log.info("파일 생성 완료: id={}", binaryContent.getId());
    return binaryContent;
  }

  @Override
  public BinaryContentDto findById(UUID id) {
    BinaryContent binaryContent = binaryContentRepository.findById(id)
        .orElseThrow(() -> new BinaryContentNotFoundException().notFoundWithId(id));
    return binaryContentMapper.toDto(binaryContent);
  }

  @Override
  public List<BinaryContentDto> findAllByIdIn(List<UUID> idList) {
    return idList.stream().map(this::findById).toList();
  }

  @Override
  public void delete(UUID id) {
    log.debug("파일 삭제 시작: id={}", id);
    if (!binaryContentRepository.existsById(id)) {
      throw new BinaryContentNotFoundException().notFoundWithId(id);
    }
    binaryContentRepository.deleteById(id);
    log.info("파일 삭제 완료: id={}", id);
  }
}
