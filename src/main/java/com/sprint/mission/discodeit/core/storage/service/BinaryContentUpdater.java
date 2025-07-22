package com.sprint.mission.discodeit.core.storage.service;

import com.sprint.mission.discodeit.core.storage.entity.BinaryContent;
import com.sprint.mission.discodeit.core.storage.entity.BinaryContentUploadStatus;
import com.sprint.mission.discodeit.core.storage.repository.JpaBinaryContentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class BinaryContentUpdater {

  private final JpaBinaryContentRepository binaryContentRepository;

  @Transactional
  public void updateStatus(BinaryContent binaryContent, BinaryContentUploadStatus status) {
    log.info("BinaryContent 상태 업데이트 {}", status);
    binaryContent.updateStatus(status);
    binaryContentRepository.save(binaryContent);
  }
}
