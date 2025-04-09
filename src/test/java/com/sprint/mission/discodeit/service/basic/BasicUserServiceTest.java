package com.sprint.mission.discodeit.service.basic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.status.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class BasicUserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private BinaryContentRepository binaryContentRepository;
    @Mock
    private UserStatusRepository userStatusRepository;
    @InjectMocks
    private BasicUserService userService;

    @Test
    @DisplayName("user 생성시 user status가 잘 생성 되는지 테스트")
    void createUserWithUserStatus() {
        // given
        CreateUserRequest request
            = new CreateUserRequest("testUser",
            "test@email.com",
            "password123");

        User createdUser = new User("testUser",
            "test@email.com",
            "password123");
        UUID userId = UUID.randomUUID();

        ReflectionTestUtils.setField(createdUser, "id", userId);
        when(userRepository.findByUserId(any(UUID.class))).thenReturn(createdUser);

        // when
        UUID resultId = userService.create(request);

        // then
        verify(userRepository).save(any(User.class));
        verify(userStatusRepository).save(any(UserStatus.class));
        assertEquals(userId, resultId);
    }

    @Test
    @DisplayName("사용자 ID로 UserStatus 조회 테스트")
    void findUserStatusByUserId() {
        // given
        UUID userId = UUID.randomUUID();
        User createdUser = new User("testUser",
            "testUser@email.com",
            "password123");
        ReflectionTestUtils.setField(createdUser, "id", userId);

        Instant now = Instant.now();
        UserStatus userStatus = new UserStatus(userId, now);

        // when
        when(userRepository.findByUserId(userId)).thenReturn(createdUser);
        when(userStatusRepository.findByUserId(userId)).thenReturn(userStatus);

        // then
        UserResponseDto response = userService.findByUserId(userId);

        assertNotNull(response);
        assertEquals(userId, response.getId());
    }
}
