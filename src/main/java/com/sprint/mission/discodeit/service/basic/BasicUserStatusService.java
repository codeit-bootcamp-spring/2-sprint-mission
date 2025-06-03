package com.sprint.mission.discodeit.service.basic;

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
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {
    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    private final UserStatusMapper userStatusMapper;

    @Transactional
    @Override
    public UserStatusDto createUserStatus(UserStatusCreateRequest request) {
        UUID userId = request.userId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> UserNotFoundException.withId(userId));

        Optional.ofNullable(user.getStatus())
                .ifPresent(status -> {
                    throw DuplicateUserStatusException.withUserId(userId);
                });
        Instant lastActiveAt = request.lastActiveAt();
        UserStatus userStatus = new UserStatus(user, lastActiveAt);
        userStatusRepository.save(userStatus);
        return userStatusMapper.toDto(userStatus);
    }

    @Override
    public UserStatusDto find(UUID userStatusId) {
        return userStatusRepository.findById(userStatusId)
                .map(userStatusMapper::toDto)
                .orElseThrow(() -> UserStatusNotFoundException.withId(userStatusId));
    }

    @Override
    public List<UserStatusDto> findAll() {
        return userStatusRepository.findAll().stream()
                .map(userStatusMapper::toDto)
                .toList();
    }

    @Transactional
    @Override
    public UserStatusDto updateUserStatus(UUID userStatusId, UserStatusUpdateRequest request) {
        Instant newLastActiveAt = request.newLastActiveAt();

        UserStatus userStatus = userStatusRepository.findById(userStatusId)
                .orElseThrow(() -> UserStatusNotFoundException.withId(userStatusId));

        userStatus.updateLastActiveAt(newLastActiveAt);

        return userStatusMapper.toDto(userStatus);
    }

    @Transactional
    @Override
    public UserStatusDto updateByUserId(UUID userId, UserStatusUpdateRequest request) {
        UserStatus userStatus = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> UserStatusNotFoundException.withId(userId));

        Instant newLastActiveAt = request.newLastActiveAt();
        userStatus.updateLastActiveAt(newLastActiveAt);

        return userStatusMapper.toDto(userStatus);
    }

    @Transactional
    @Override
    public void deleteUserStatus(UUID userStatusId) {
        if (!userStatusRepository.existsById(userStatusId)) {
            throw UserStatusNotFoundException.withId(userStatusId);
        }
        userStatusRepository.deleteById(userStatusId);
    }
}
