package com.sprint.mission.discodeit.domain.notification;

import com.sprint.mission.discodeit.common.failure.AsyncTaskFailureRepository;
import com.sprint.mission.discodeit.security.auth.dto.RoleUpdateRequest;
import com.sprint.mission.discodeit.security.auth.service.AuthService;
import com.sprint.mission.discodeit.domain.notification.entity.Notification;
import com.sprint.mission.discodeit.domain.notification.entity.NotificationType;
import com.sprint.mission.discodeit.domain.notification.repository.NotificationRepository;
import com.sprint.mission.discodeit.domain.user.entity.Role;
import com.sprint.mission.discodeit.domain.user.entity.User;
import com.sprint.mission.discodeit.domain.user.repository.UserRepository;
import com.sprint.mission.discodeit.testutil.IntegrationTestSupport;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class NotificationTest extends IntegrationTestSupport {

  @Autowired
  private AuthService authService;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private NotificationRepository notificationRepository;
  @Autowired
  private AsyncTaskFailureRepository asyncTaskFailureRepository;

  @BeforeEach
  void setUp() {
    notificationRepository.deleteAllInBatch();
    asyncTaskFailureRepository.deleteAllInBatch();
  }

  @DisplayName("유저의 역할 변경이 일어나면, 알림을 발행합니다.")
  @Test
  void test_Notification() {
    // given
    User savedUser = userRepository.save(new User("", "", "", null));
    RoleUpdateRequest roleUpdateRequest = new RoleUpdateRequest(savedUser.getId(),
        Role.CHANNEL_MANAGER);

    // when
    authService.updateRole(roleUpdateRequest);

    // when & then
    Assertions.assertThat(notificationRepository.findAllByReceiverId(savedUser.getId()))
        .hasSize(1)
        .extracting(Notification::getType)
        .containsExactly(NotificationType.ROLE_CHANGED);
  }

}
