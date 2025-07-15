package com.sprint.mission.discodeit.core.recover.service;

import com.sprint.mission.discodeit.core.recover.entity.AsyncTaskFailure;
import com.sprint.mission.discodeit.core.recover.repository.AsyncTaskFailureRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicRecoverService implements RecoverService {

  private final AsyncTaskFailureRepository asyncTaskFailureRepository;

  @Override
  @Transactional
  public void write(UUID id, String message) {
    String requestId = MDC.get("requestId");
    if (requestId == null) {
      requestId = "N/A"; // 비동기 환경에서 누락될 경우를 대비
    }
    AsyncTaskFailure failureLog = new AsyncTaskFailure(
        "File Upload",
        requestId,
        String.format("Failed to upload file with ID: %s. Error: %s", id, message)
    );
    asyncTaskFailureRepository.save(failureLog);
    log.info("성공적으로 오류가 기록되었습니다.");

  }
}
