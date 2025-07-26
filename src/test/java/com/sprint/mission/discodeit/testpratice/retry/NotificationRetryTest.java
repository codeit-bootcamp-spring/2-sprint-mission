package com.sprint.mission.discodeit.testpratice.retry;

import static com.sprint.mission.discodeit.common.filter.constant.LogConstant.REQUEST_ID;
import static org.mockito.ArgumentMatchers.any;

import com.sprint.mission.discodeit.common.failure.AsyncTaskFailure;
import com.sprint.mission.discodeit.common.failure.AsyncTaskFailureRepository;
import com.sprint.mission.discodeit.domain.auth.dto.RoleUpdateRequest;
import com.sprint.mission.discodeit.domain.auth.service.AuthService;
import com.sprint.mission.discodeit.domain.notification.repository.NotificationRepository;
import com.sprint.mission.discodeit.domain.user.entity.Role;
import com.sprint.mission.discodeit.domain.user.entity.User;
import com.sprint.mission.discodeit.domain.user.repository.UserRepository;
import com.sprint.mission.discodeit.testutil.IntegrationTestSupport;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.shaded.org.awaitility.Awaitility;

public class NotificationRetryTest extends IntegrationTestSupport {

  @Autowired
  private AuthService authService;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private AsyncTaskFailureRepository asyncTaskFailureRepository;

  @MockitoBean
  private NotificationRepository notificationRepository;

  @BeforeEach
  void setUp() {
    notificationRepository.deleteAllInBatch();
    asyncTaskFailureRepository.deleteAllInBatch();
  }

  @DisplayName("알림 생성이 실패하면, 재시도를 합니다.")
  @Test
  void test_Retry() {
    // given
    String requestId = UUID.randomUUID().toString();
    MDC.put(REQUEST_ID, requestId);
    User savedUser = userRepository.save(new User("", "", "", null));
    RoleUpdateRequest roleUpdateRequest = new RoleUpdateRequest(savedUser.getId(),
        Role.CHANNEL_MANAGER);
    BDDMockito.given(notificationRepository.save(any())).willThrow(IllegalArgumentException.class);

    // when
    authService.updateRole(roleUpdateRequest);

    // then
    Awaitility.await()
        .atMost(4, TimeUnit.SECONDS)
        .untilAsserted(() ->
            Assertions.assertThat(asyncTaskFailureRepository.findAll())
                .hasSize(1)
                .extracting(AsyncTaskFailure::getRequestId)
                .containsExactly(requestId)
        );
  }

}
