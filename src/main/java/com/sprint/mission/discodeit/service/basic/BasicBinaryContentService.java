package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentMapper binaryContentMapper;
  private final BinaryContentStorage binaryContentStorage;

  @Transactional
  @Override
  public BinaryContentDto create(BinaryContentCreateRequest request) {
    log.debug("이진 콘텐츠 저장 요청: filename={}", request.fileName());
    BinaryContent binary = new BinaryContent(request.fileName(), (long) request.bytes().length,
        request.contentType());
    binaryContentRepository.save(binary);
    binaryContentStorage.put(binary.getId(), request.bytes());
    log.info("이진 콘텐츠 저장 완료: id={}", binary.getId());
    return binaryContentMapper.toDto(binary);
  }

  @Override
  public BinaryContentDto find(UUID binaryContentId) {
    log.debug("이진 콘텐츠 조회 요청: id={}", binaryContentId);
    return binaryContentRepository.findById(binaryContentId)
        .map(binaryContentMapper::toDto)
        .orElseThrow(() -> {
          log.warn("이진 콘텐츠 조회 실패 - 없음: id={}", binaryContentId);
          return new NoSuchElementException(
              "BinaryContent with id " + binaryContentId + " not found");
        });
  }

  @Override
  public List<BinaryContentDto> findAllByIdIn(List<UUID> binaryContentIds) {
    log.debug("다중 이진 콘텐츠 조회 요청: count={}", binaryContentIds.size());
    return binaryContentRepository.findAllById(binaryContentIds).stream()
        .map(binaryContentMapper::toDto)
        .toList();
  }

  @Transactional
  @Override
  public void delete(UUID binaryContentId) {
    log.debug("이진 콘텐츠 삭제 요청: id={}", binaryContentId);
    if (!binaryContentRepository.existsById(binaryContentId)) {
      log.warn("삭제 실패 - 존재하지 않음: id={}", binaryContentId);
      throw new NoSuchElementException("BinaryContent with id " + binaryContentId + " not found");
    }

    binaryContentRepository.deleteById(binaryContentId);
    log.info("이진 콘텐츠 삭제 완료: id={}", binaryContentId);
  }
}

