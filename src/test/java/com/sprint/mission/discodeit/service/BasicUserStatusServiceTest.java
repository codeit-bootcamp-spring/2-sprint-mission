package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.exception.userStatus.DuplicateUserStatusException;
import com.sprint.mission.discodeit.exception.userStatus.UserStatusNotFoundException;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.basic.BasicUserStatusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BasicUserStatusServiceTest {
    @Mock
    private UserStatusRepository userStatusRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserStatusMapper userStatusMapper;

    @InjectMocks
    private BasicUserStatusService userStatusService;

    private UUID userStatusId;
    private UUID userId;
    private Instant lastActiveAt;
    private User user;
    private UserStatus userStatus;
    private UserStatusDto userStatusDto;

    @BeforeEach
    void setUp() {
        userStatusId = UUID.randomUUID();
        userId = UUID.randomUUID();
        lastActiveAt = Instant.now();

        user = new User("testUser", "test@example.com", "password", null);
        ReflectionTestUtils.setField(user, "id", userId);

        userStatus = new UserStatus(user, lastActiveAt);
        ReflectionTestUtils.setField(userStatus, "id", userStatusId);

        userStatusDto = new UserStatusDto(userStatusId, userId, lastActiveAt);
    }

    @Test
    @DisplayName("사용자 상태 생성 성공")
    void createUserStatus_WithValidInput_Success() {
        //given
        UserStatusCreateRequest request = new UserStatusCreateRequest(userId, lastActiveAt);
        given(userRepository.findById(eq(userId))).willReturn(Optional.of(user));
        given(userStatusMapper.toDto(any(UserStatus.class))).willReturn(userStatusDto);

        //사용자에게 기존 상태가 없어야 한다.
        ReflectionTestUtils.setField(user, "status", null);

        //when
        UserStatusDto result = userStatusService.createUserStatus(request);

        //then
        assertThat(result).isEqualTo(userStatusDto);
        verify(userStatusRepository).save(any(UserStatus.class));
    }

    @Test
    @DisplayName("이미 상태가 있는 사용자에 대해 상태 생성 시도 시 예외 발생")
    void createUserStatus_WithExistingStatus_ThrowException() {
        //given
        UserStatusCreateRequest request = new UserStatusCreateRequest(userId, lastActiveAt);
        given(userRepository.findById(eq(userId))).willReturn(Optional.of(user));

        //사용자에게 이미 상태가 있음
        ReflectionTestUtils.setField(user, "status", userStatus);

        //when & then
        assertThatThrownBy(() -> userStatusService.createUserStatus(request))
                .isInstanceOf(DuplicateUserStatusException.class);
    }

    @Test
    @DisplayName("존재하지 않는 사용자에 대해 상태 생성 시도 시 예외 발생")
    void createUserStatus_WithInValidUserId_ThrowException() {
        //given
        UserStatusCreateRequest request = new UserStatusCreateRequest(userId, lastActiveAt);
        given(userRepository.findById(eq(userId))).willReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> userStatusService.createUserStatus(request))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("사용자 상태 조회 성공")
    void findUserStatus_Success() {
        //given
        given(userStatusRepository.findById(eq(userStatusId))).willReturn(Optional.of(userStatus));
        given(userStatusMapper.toDto(any(UserStatus.class))).willReturn(userStatusDto);

        //when
        UserStatusDto result = userStatusService.find(userStatusId);

        //then
        assertThat(result).isEqualTo(userStatusDto);
    }

    @Test
    @DisplayName("존재하지 않는 사용자 상태 조회 시 예외 발생")
    void findUserStatus_WithInvalidUserStatusId_ThrowException() {
        //given
        given(userStatusRepository.findById(eq(userStatusId))).willReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> userStatusService.find(userStatusId))
                .isInstanceOf(UserStatusNotFoundException.class);
    }

    @Test
    @DisplayName("전체 사용자 상태 목록 조회 성공")
    void findAllUserStatuses_Success() {
        //given
        given(userStatusRepository.findAll()).willReturn(List.of(userStatus));
        given(userStatusMapper.toDto(any(UserStatus.class))).willReturn(userStatusDto);

        //when
        List<UserStatusDto> result = userStatusService.findAll();

        //then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(userStatusDto);
    }

    @Test
    @DisplayName("사용자 상태 update 성공")
    void updateUserStatus_Success() {
        //given
        Instant newLastActiveAt = Instant.now().plusSeconds(60);
        UserStatusUpdateRequest request = new UserStatusUpdateRequest(newLastActiveAt);

        given(userStatusRepository.findById(eq(userStatusId))).willReturn(Optional.of(userStatus));
        given(userStatusMapper.toDto(any(UserStatus.class))).willReturn(userStatusDto);

        //when
        UserStatusDto result = userStatusService.updateUserStatus(userStatusId, request);

        //then
        assertThat(result).isEqualTo(userStatusDto);
    }

    @Test
    @DisplayName("존재하지 않는 사용자 상태 업데이트 시도 시 예외 발생")
    void updateUserStatus_WithInvalidUserStatusId_ThrowException() {
        //given
        Instant newLastActiveAt = Instant.now().plusSeconds(60);
        UserStatusUpdateRequest request = new UserStatusUpdateRequest(newLastActiveAt);

        given(userStatusRepository.findById(userStatusId)).willReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> userStatusService.updateUserStatus(userStatusId, request))
                .isInstanceOf(UserStatusNotFoundException.class);
    }

    @Test
    @DisplayName("user id로 상태 update 성공")
    void updateUserStatusByUserId_Success() {
        //given
        Instant newLastActiveAt = Instant.now().plusSeconds(60);
        UserStatusUpdateRequest request = new UserStatusUpdateRequest(newLastActiveAt);

        given(userStatusRepository.findByUserId(eq(userId))).willReturn(Optional.of(userStatus));
        given(userStatusMapper.toDto(any(UserStatus.class))).willReturn(userStatusDto);

        //when
        UserStatusDto result = userStatusService.updateByUserId(userId, request);

        //then
        assertThat(result).isEqualTo(userStatusDto);
    }

    @Test
    @DisplayName("존재하지 않는 user id로 상태 update 시 예외 발생")
    void updateUserStatusByUserId_WithInvalidUserId_ThrowException() {
        //given
        Instant newLastActiveAt = Instant.now().plusSeconds(60);
        UserStatusUpdateRequest request = new UserStatusUpdateRequest(newLastActiveAt);

        given(userStatusRepository.findByUserId(eq(userId))).willReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> userStatusService.updateByUserId(userId, request))
                .isInstanceOf(UserStatusNotFoundException.class);
    }

    @Test
    @DisplayName("사용자 상태 삭제 성공")
    void deleteUserStatus_WithValidInput_Success() {
        //given
        given(userStatusRepository.existsById(eq(userStatusId))).willReturn(true);

        //when
        userStatusService.deleteUserStatus(userStatusId);

        //then
        verify(userStatusRepository).deleteById(userStatusId);

    }

    @Test
    @DisplayName("존재하지 않는 사용자 상태 삭제 시 예외 발생")
    void deleteUserStatus_WithInvalidUserStatusId_ThrowException() {
        //given
        given(userStatusRepository.existsById(eq(userStatusId))).willReturn(false);

        //when & then
        assertThatThrownBy(() -> userStatusService.deleteUserStatus(userStatusId))
                .isInstanceOf(UserStatusNotFoundException.class);
    }
}
