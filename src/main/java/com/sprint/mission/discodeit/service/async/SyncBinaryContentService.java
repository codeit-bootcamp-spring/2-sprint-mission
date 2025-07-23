package com.sprint.mission.discodeit.service.async;

import com.sprint.mission.discodeit.domain.BinaryContent;
import com.sprint.mission.discodeit.domain.BinaryContentUploadStatus;
import com.sprint.mission.discodeit.exception.binaryContent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SyncBinaryContentService {

  private final BinaryContentProcessor binaryContentProcessor;
  private final BinaryContentRepository binaryContentRepository;
  
  @Transactional
  public void uploadBinaryContent(UUID contentId, byte[] bytes) {
    String taskName = "SyncBinaryContentService.uploadBinaryContent";
    try {
      // 1. 실제 파일 저장 로직 호출
      binaryContentProcessor.processUpload(contentId, bytes);

      // 2. 성공 상태 업데이트 (같은 트랜잭션 내에서 처리)
      BinaryContent content = binaryContentRepository.findById(contentId)
          .orElseThrow(() -> BinaryContentNotFoundException.byId(contentId));
      content.updateUploadStatus(BinaryContentUploadStatus.SUCCESS);
      binaryContentRepository.save(content);

    } catch (Exception e) {
      log.error("Sync upload failed for ID {}: {}", contentId, e.getMessage());
      binaryContentProcessor.handleFailure(contentId, taskName, e.getMessage());

      // RuntimeException으로 감싸서 던져주어야 @Transactional이 롤백 수행
      throw new RuntimeException("Sync file upload failed", e);
    }
  }
}