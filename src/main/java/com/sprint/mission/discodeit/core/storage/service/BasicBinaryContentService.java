package com.sprint.mission.discodeit.core.storage.service;

import com.sprint.mission.discodeit.core.storage.BinaryContentException;
import com.sprint.mission.discodeit.core.storage.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.core.storage.entity.BinaryContent;
import com.sprint.mission.discodeit.core.storage.entity.BinaryContentUploadStatus;
import com.sprint.mission.discodeit.core.storage.repository.JpaBinaryContentRepository;
import com.sprint.mission.discodeit.exception.ErrorCode;
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

  private final JpaBinaryContentRepository binaryContentMetaRepository;
  private final FileUploadService fileUploadService;
  private final BinaryContentUpdater binaryContentUpdater;

  @Override
  @Transactional
  public BinaryContent create(BinaryContentCreateRequest request) throws IOException {
    if (request == null) {
      log.warn("[BinaryContentService] Parameter is empty");
      return null;
    }
    BinaryContent binaryContent = BinaryContent.create(request.fileName(),
        (long) request.bytes().length, request.contentType());
    binaryContentMetaRepository.save(binaryContent);
    UUID binaryContentId = binaryContent.getId();
    log.info("[BinaryContentService] Binary Content Created: {}. 비동기 업로드를 시작합니다.",
        binaryContentId);

    fileUploadService.uploadWithRetry(binaryContentId, request.bytes())
        .whenComplete((result, ex) -> {
          if (ex == null) {
            log.info("파일 업로드 성공 : 스레드명 {}", Thread.currentThread().getName());
            binaryContentUpdater.updateStatus(binaryContent, BinaryContentUploadStatus.SUCCESS);
          } else {
            log.error("파일 업로드 실패 : 스레드명 {}, 이유 {}", Thread.currentThread().getName(),
                ex.getMessage());
            binaryContentUpdater.updateStatus(binaryContent, BinaryContentUploadStatus.FAILED);
          }
        });

    return binaryContent;
  }

  @Override
  @Transactional(readOnly = true)
  public BinaryContent findById(UUID binaryId) {
    return binaryContentMetaRepository.findById(binaryId).orElseThrow(
        () -> new BinaryContentException(ErrorCode.FILE_NOT_FOUND, binaryId)
    );
  }

  @Override
  @Transactional(readOnly = true)
  public List<BinaryContent> findAllByIdIn(List<UUID> binaryContentIds) {
    return binaryContentMetaRepository.findAllByIdIn((binaryContentIds));
  }

  @Override
  @Transactional
  public void delete(UUID binaryId) {
    BinaryContent binaryContent = binaryContentMetaRepository.findById(binaryId).orElseThrow(
        () -> new BinaryContentException(ErrorCode.FILE_NOT_FOUND, binaryId)
    );
    binaryContentMetaRepository.delete(binaryContent);
    log.info("[BinaryContentService] Binary Content deleted successfully");
  }
}
