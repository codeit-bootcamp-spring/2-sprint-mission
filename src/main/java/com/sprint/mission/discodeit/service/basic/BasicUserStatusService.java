package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserStatusDto;
import com.sprint.mission.discodeit.dto.request.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.exception.userstatus.UserStatusAlreadyExistsException;
import com.sprint.mission.discodeit.exception.userstatus.UserStatusNotFoundException;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Transactional
public class BasicUserStatusService implements UserStatusService {
    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;
    private final UserStatusMapper userStatusMapper;

    @Override
    public UserStatusDto create(UserStatusCreateRequest request) {
        UUID userId = request.userId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        if (user.getStatus() != null) {
            throw new UserStatusAlreadyExistsException(userId);
        }

        Instant lastActiveAt = request.lastActiveAt();

        UserStatus userStatus = UserStatus.builder()
                .user(user)
                .lastActiveAt(lastActiveAt)
                .build();

        UserStatus savedUserStatus = userStatusRepository.save(userStatus);
        return userStatusMapper.toDto(savedUserStatus);
    }

    @Override
    public UserStatusDto find(UUID userStatusId) {
        UserStatus userStatus = userStatusRepository.findById(userStatusId)
                .orElseThrow(() -> UserStatusNotFoundException.forUserStatusId(userStatusId));

        return userStatusMapper.toDto(userStatus);
    }

    @Override
    public List<UserStatusDto> findAll() {
        List<UserStatus> userStatuses = userStatusRepository.findAll();
        return userStatusMapper.toDto(userStatuses);
    }

    @Override
    public UserStatusDto update(UUID userStatusId, UserStatusUpdateRequest request) {
        Instant newLastActiveAt = request.newLastActiveAt();

        UserStatus userStatus = userStatusRepository.findById(userStatusId)
                .orElseThrow(() -> UserStatusNotFoundException.forUserStatusId(userStatusId));

        userStatus.updateLastActiveAt(newLastActiveAt);

        UserStatus updatedUserStatus = userStatusRepository.save(userStatus);
        return userStatusMapper.toDto(updatedUserStatus);
    }

    @Override
    public UserStatusDto updateByUserId(UUID userId, UserStatusUpdateRequest request) {
        Instant newLastActiveAt = request.newLastActiveAt();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        UserStatus userStatus = Optional.ofNullable(user.getStatus())
                .orElseThrow(() -> UserStatusNotFoundException.forUserStatusId(userId));

        userStatus.updateLastActiveAt(newLastActiveAt);

        UserStatus updatedUserStatus = userStatusRepository.save(userStatus);
        return userStatusMapper.toDto(updatedUserStatus);
    }

    @Override
    public void delete(UUID userStatusId) {
        if (!userStatusRepository.existsById(userStatusId)) {
            throw UserStatusNotFoundException.forUserStatusId(userStatusId);
        }
        userStatusRepository.deleteById(userStatusId);
    }
}