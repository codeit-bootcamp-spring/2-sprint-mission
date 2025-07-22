package com.sprint.mission.discodeit.core.recover.service;

import com.sprint.mission.discodeit.core.auth.entity.CustomUserDetails;
import com.sprint.mission.discodeit.core.notification.dto.NotificationEvent;
import com.sprint.mission.discodeit.core.notification.entity.NotificationTitle;
import com.sprint.mission.discodeit.core.notification.entity.NotificationType;
import com.sprint.mission.discodeit.core.recover.entity.AsyncTaskFailure;
import com.sprint.mission.discodeit.core.recover.repository.AsyncTaskFailureRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicRecoverService implements RecoverService {

  private final AsyncTaskFailureRepository asyncTaskFailureRepository;
  private final ApplicationEventPublisher eventPublisher; // 이벤트 발행을 위한 객체

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

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.isAuthenticated()) {
      CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
      UUID userId = userDetails.getUserDto().id();
      eventPublisher.publishEvent(
          new NotificationEvent(userId, NotificationTitle.FAILED.getTitle(), "이미지 업로드에 실패하였습니다.",
              NotificationType.ASYNC_FAILED, null));
    }
  }
}
