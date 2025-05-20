package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentResponse;
import com.sprint.mission.discodeit.entity.common.BinaryContent;
import com.sprint.mission.discodeit.exception.binarycontent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;
  private final BinaryContentMapper binaryContentMapper;

  @Override
  public BinaryContent create(BinaryContentCreateRequest request) {
    log.info("바이너리 콘텐츠 (파일) 업로드 시도: fileName={}, contentType={}, size={}", request.fileName(),
        request.contentType(), request.bytes().length);
    BinaryContent binaryContent = new BinaryContent(
        request.fileName(),
        (long) request.bytes().length,
        request.contentType());

    binaryContentRepository.save(binaryContent);
    log.debug("바이너리 콘텐츠 엔티티 저장 완료: binaryContentId={}", binaryContent.getId());

    binaryContentStorage.put(binaryContent.getId(), request.bytes());
    log.info("바이너리 콘텐츠 (파일) 업로드 성공: binaryContentId={}", binaryContent.getId());
    return binaryContent;
  }

  @Override
  public BinaryContent find(UUID binaryContentId) {
    log.debug("바이너리 콘텐츠 조회 시도: binaryContentId={}", binaryContentId);
    return binaryContentRepository.findById(binaryContentId)
        .orElseThrow(() -> {
          log.warn("바이너리 콘텐츠 조회 실패 - 콘텐츠를 찾을 수 없음: binaryContentId={}", binaryContentId);
          return new BinaryContentNotFoundException();
        });
  }

  @Override
  public List<BinaryContent> findAllByIdIn(List<UUID> binaryContentIds) {
    log.debug("여러 바이너리 콘텐츠 조회 시도: binaryContentIds={}", binaryContentIds);
    List<BinaryContent> binaryContents = binaryContentRepository.findAllByIdIn(binaryContentIds);
    log.debug("여러 바이너리 콘텐츠 조회 성공: {}개 발견", binaryContents.size());
    return binaryContents;
  }

  @Override
  public void delete(BinaryContent binaryContent) {
    log.info("바이너리 콘텐츠 (파일) 삭제 시도: binaryContentId={}", binaryContent.getId());
    BinaryContent found = binaryContentRepository.findById(binaryContent.getId())
        .orElseThrow(() -> {
          log.warn("바이너리 콘텐츠 삭제 실패 - 콘텐츠를 찾을 수 없음: binaryContentId={}", binaryContent.getId());
          return new BinaryContentNotFoundException();
        });

    binaryContentStorage.delete(found.getId());
    log.debug("스토리지에서 바이너리 콘텐츠 삭제 완료: binaryContentId={}", found.getId());
    binaryContentRepository.delete(found);
    log.info("바이너리 콘텐츠 (파일) 삭제 성공: binaryContentId={}", found.getId());
  }

  @Override
  public ResponseEntity<?> download(UUID binaryContentId) {
    log.info("바이너리 콘텐츠 (파일) 다운로드 시도: binaryContentId={}", binaryContentId);
    BinaryContent binaryContent = find(binaryContentId);
    if (binaryContent == null) {
      log.warn("바이너리 콘텐츠 다운로드 실패 - 콘텐츠를 찾을 수 없음: binaryContentId={}", binaryContentId);
      throw new BinaryContentNotFoundException();
    }

    BinaryContentResponse binaryContentResponse = binaryContentMapper.toDto(binaryContent);
    log.info("바이너리 콘텐츠 (파일) 다운로드 시작: binaryContentId={}", binaryContentId);
    return binaryContentStorage.download(binaryContentResponse);
  }
}