package com.sprint.mission.discodeit.service.basic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.sprint.mission.discodeit.dto.userstatus.CreateUserStatusRequest;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BasicUserStatusServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserStatusRepository userStatusRepository;
    @Mock
    private UserStatusMapper userStatusMapper;
    @InjectMocks
    private BasicUserStatusService userStatusService;

    @Test
    @DisplayName("정상적으로 UserStatus가 생성되는 경우")
    void create_success() {

        // given
        UUID userId = UUID.randomUUID();
        Instant lastActiveAt = Instant.now();

        CreateUserStatusRequest request =
            new CreateUserStatusRequest(userId, lastActiveAt);

        User user = User.createUser("user1", "user1@email.com", "password");
        UserStatus userStatus = UserStatus.createUserStatus(user, lastActiveAt);

        UserStatusDto dto = new UserStatusDto(userStatus.getId(), user.getId(),
            lastActiveAt);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userStatusRepository.existsByUserId(userId)).thenReturn(false);

        when(userStatusRepository.save(any(UserStatus.class))).thenReturn(userStatus);
        when(userStatusMapper.toDto(any(UserStatus.class))).thenReturn(dto);

        // when
        UserStatusDto result = userStatusService.create(request);

        // then
        assertThat(result).isEqualTo(dto);
    }
}
