package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.BinaryContentUploadStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UploadStatusService {

  private final BinaryContentRepository binaryContentRepository;

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void updateUploadStatus(UUID binaryContentId, BinaryContentUploadStatus status) {
    log.debug("저장소에 파일 업로드 : {}", binaryContentId);
    binaryContentRepository.updateUploadStatus(binaryContentId, status);
  }
}
