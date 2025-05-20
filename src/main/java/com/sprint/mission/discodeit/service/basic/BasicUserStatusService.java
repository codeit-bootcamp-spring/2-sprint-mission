package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.status.CreateUserStatusRequest;
import com.sprint.mission.discodeit.dto.status.UpdateUserStatusRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.exception.userstatus.UserStatusAlreadyExistsException;

import com.sprint.mission.discodeit.exception.userstatus.UserStatusNotFoundException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;

    @Override
    @Transactional
    public UserStatus create(CreateUserStatusRequest request) {
        UUID userId = request.userId();

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(Map.of("userId", userId)));

        if (userStatusRepository.existsById(userId)) {
            throw new UserStatusAlreadyExistsException(Map.of("userId", userId));
        }

        UserStatus userStatus = new UserStatus(user);
        userStatusRepository.save(userStatus);
        return userStatus;
    }

    @Override
    @Transactional(readOnly = true)
    public UserStatus getById(UUID userId) {
        return userStatusRepository.findById(userId)
            .orElseThrow(() -> new UserStatusNotFoundException(Map.of("userId", userId)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserStatus> getAll() {
        return userStatusRepository.findAll();
    }

    @Override
    @Transactional
    public void update(UUID userId, UpdateUserStatusRequest request) {
        UserStatus userStatus = userStatusRepository.findByUserId(userId)
            .orElseGet(() -> {
                User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException(Map.of("userId", userId)));
                return userStatusRepository.save(new UserStatus(user));
            });

        userStatus.updateLastActiveAt(request.newLastActiveAt());
    }

    @Override
    @Transactional
    public void updateByUserId(UUID userId) {
        UserStatus userStatus = userStatusRepository.findById(userId)
            .orElseGet(() -> {
                User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException(Map.of("userId", userId)));
                return userStatusRepository.save(new UserStatus(user));
            });
        userStatus.updateLastActiveAt(Instant.now());
    }

    @Override
    @Transactional
    public void delete(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserStatusNotFoundException(Map.of("userId", userId));
        }
        userStatusRepository.deleteById(userId);
    }
}
