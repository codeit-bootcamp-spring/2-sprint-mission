package com.sprint.mission.discodeit.userstatus.service;

import com.sprint.mission.discodeit.user.entity.User;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import com.sprint.mission.discodeit.userstatus.dto.UserStatusResult;
import com.sprint.mission.discodeit.userstatus.service.UserStatusService;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.UUID;


@ActiveProfiles("test")
@SpringBootTest
class BasicUserStatusServiceTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserStatusService userStatusService;

    @DisplayName("유저의 마지막 활동시간을 업데이트합니다.")
    @Test
    void updateByUserId() {
        // given
        User user = userRepository.save(new User("", "", "", null));
        Instant instant = Instant.parse("2025-05-17T14:00:00Z");

        // when
        UserStatusResult userStatusResult = userStatusService.updateByUserId(user.getId(), instant);

        // then
        Assertions.assertThat(userStatusResult.lastActiveAt()).isEqualTo(instant);
    }

    @DisplayName("업데이트시 유저가 없으면, 예외를 반환합니다.")
    @Test
    void updateByUserId_Exception() {
        // given
        Instant instant = Instant.parse("2025-05-17T14:00:00Z");

        // when & then
        Assertions.assertThat(userStatusService.updateByUserId(UUID.randomUUID(), instant))
                .isInstanceOf(EntityNotFoundException.class);
    }

}