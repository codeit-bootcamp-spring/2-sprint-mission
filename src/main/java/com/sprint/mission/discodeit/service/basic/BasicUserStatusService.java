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
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BasicUserStatusService implements UserStatusService {

    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;
    private final UserStatusMapper userStatusMapper;

    @Transactional
    @Override
    public UserStatusDto create(UserStatusCreateRequest request) {
        UUID userId = request.userId();

        User user = userRepository.findById(userId).orElseThrow(
            () -> new NoSuchElementException("User with id " + userId + " does not exist"));

        if (user.getUserStatus() != null) {
            throw new IllegalArgumentException("UserStatus for user " + userId + " already exists");
        }

        UserStatus status = new UserStatus();
        status.setLastActiveAt(request.lastActiveAt());
        user.setUserStatus(status);

        userRepository.save(user);

        return userStatusMapper.toDto(status);
    }

    @Transactional(readOnly = true)
    @Override
    public UserStatusDto find(UUID userStatusId) {
        UserStatus userStatus = userStatusRepository.findById(userStatusId).orElseThrow(
            () -> new NoSuchElementException("UserStatus with id " + userStatusId + " not found"));
        return userStatusMapper.toDto(userStatus);
    }

    @Override
    public List<UserStatus> findAll() {
        return userStatusRepository.findAll().stream().toList();
    }

    @Transactional
    @Override
    public UserStatusDto update(UUID userStatusId, UserStatusUpdateRequest request) {
        Instant newLastActiveAt = request.newLastActiveAt();

        UserStatus userStatus = userStatusRepository.findById(userStatusId).orElseThrow(
            () -> new NoSuchElementException("UserStatus with id " + userStatusId + " not found"));
        userStatus.setLastActiveAt(newLastActiveAt);

        userStatusRepository.save(userStatus);
        return userStatusMapper.toDto(userStatus);
    }

    @Override
    public UserStatusDto updateByUserId(UUID userId, UserStatusUpdateRequest request) {
        Instant newLastActiveAt = request.newLastActiveAt();

        UserStatus userStatus = userStatusRepository.findByUserId(userId).orElseThrow(
            () -> new NoSuchElementException("UserStatus with userId " + userId + " not found"));
        userStatus.setLastActiveAt(newLastActiveAt);
        userStatusRepository.save(userStatus);
        return userStatusMapper.toDto(userStatus);
    }

    @Override
    public void delete(UUID userStatusId) {
        if (!userStatusRepository.existsById(userStatusId)) {
            throw new NoSuchElementException("UserStatus with id " + userStatusId + " not found");
        }
        userStatusRepository.deleteById(userStatusId);
    }
}
