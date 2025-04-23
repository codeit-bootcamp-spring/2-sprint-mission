package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserStatusDto;
import com.sprint.mission.discodeit.dto.request.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
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
                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " does not exist"));

        if (user.getStatus() != null) {
            throw new IllegalArgumentException("UserStatus for user " + userId + " already exists");
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
                .orElseThrow(() -> new NoSuchElementException("UserStatus with id " + userStatusId + " not found"));

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
                .orElseThrow(() -> new NoSuchElementException("UserStatus with id " + userStatusId + " not found"));

        userStatus.updateLastActiveAt(newLastActiveAt);

        UserStatus updatedUserStatus = userStatusRepository.save(userStatus);
        return userStatusMapper.toDto(updatedUserStatus);
    }

    @Override
    public UserStatusDto updateByUserId(UUID userId, UserStatusUpdateRequest request) {
        Instant newLastActiveAt = request.newLastActiveAt();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));

        UserStatus userStatus = Optional.ofNullable(user.getStatus())
                .orElseThrow(() -> new NoSuchElementException("UserStatus for user " + userId + " not found"));

        userStatus.updateLastActiveAt(newLastActiveAt);

        UserStatus updatedUserStatus = userStatusRepository.save(userStatus);
        return userStatusMapper.toDto(updatedUserStatus);
    }

    @Override
    public void delete(UUID userStatusId) {
        if (!userStatusRepository.existsById(userStatusId)) {
            throw new NoSuchElementException("UserStatus with id " + userStatusId + " not found");
        }
        userStatusRepository.deleteById(userStatusId);
    }
}