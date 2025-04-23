package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userstatus.CreateUserStatusRequest;
import com.sprint.mission.discodeit.dto.userstatus.UpdateUserStatusRequest;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.custom.DuplicateResourceException;
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

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    private final UserStatusMapper userStatusMapper;

    @Override
    @Transactional
    public UserStatusDto create(CreateUserStatusRequest request) {
        UUID userId = request.userId();

        User user = userRepository.findById(userId).orElseThrow(null);
        if (user == null) {
            throw new NoSuchElementException("[ERROR] user not found");
        }

        if (userStatusRepository.existsByUserId(request.userId())) {
            throw new DuplicateResourceException("[ERROR] user status is already exist");
        }

        Instant lastActiveAt = request.lastActiveAt();
        UserStatus userStatus = UserStatus.createUserStatus(user, lastActiveAt);

        userStatusRepository.save(userStatus);

        return userStatusMapper.toDto(userStatus);
    }

    @Override
    @Transactional
    public UserStatusDto findByUserStatusId(UUID userStatusId) {
        UserStatus userStatus = userStatusRepository.findById(userStatusId).orElse(null);
        if (userStatus == null) {
            throw new NoSuchElementException("[ERROR] user status not found");
        }

        return userStatusMapper.toDto(userStatus);
    }

    @Override
    @Transactional
    public List<UserStatusDto> findAll() {
        return userStatusRepository.findAll().stream()
            .map(userStatusMapper::toDto).toList();
    }

    @Override
    @Transactional
    public UserStatusDto update(UUID userStatusId, UpdateUserStatusRequest request) {
        Instant newLastActiveAt = request.newLastActiveAt();

        UserStatus userStatus = userStatusRepository.findById(userStatusId).orElse(null);
        if (userStatus == null) {
            throw new NoSuchElementException("[ERROR] user status not found");
        }
        userStatus.updateLastActiveAt(newLastActiveAt);

        return userStatusMapper.toDto(userStatus);
    }


    @Override
    @Transactional
    public UserStatusDto updateByUserId(UUID userId, UpdateUserStatusRequest request) {
        Instant newLastActiveAt = request.newLastActiveAt();

        UserStatus userStatus = userStatusRepository.findByUserId(userId);
        if (userStatus == null) {
            throw new NoSuchElementException("[ERROR] user status not found");
        }

        userStatus.updateLastActiveAt(newLastActiveAt);

        return userStatusMapper.toDto(userStatus);
    }

    @Override
    @Transactional
    public void delete(UUID userId) {
        userStatusRepository.deleteById(userId);
    }
}
