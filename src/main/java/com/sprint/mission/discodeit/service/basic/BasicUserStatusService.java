package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateRequest;
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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {
    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    private final UserStatusMapper userStatusMapper;

    @Transactional
    @Override
    public UserStatus createUserStatus(UserStatusCreateRequest dto) {
        UUID userId = dto.userId();
        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("존재하지 않는 사용자입니다."));

        if (userStatusRepository.findByUser_Id(userId).isPresent()) {
            throw new IllegalStateException("이미 존재하는 사용자 상태입니다.");
        }
        Instant lastActiveAt = dto.lastActiveAt();
        UserStatus userStatus = new UserStatus(user, lastActiveAt);
        return userStatusRepository.save(userStatus);
    }

    @Transactional(readOnly = true)
    @Override
    public UserStatus findById(UUID userStatusId) {
        return userStatusRepository.findById(userStatusId)
                .orElseThrow(() -> new NoSuchElementException("UserStatus with id " + userStatusId + " not found"));
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserStatus> findAll() {
        return userStatusRepository.findAll();
    }

    @Transactional
    @Override
    public UserStatus updateUserStatus(UUID userStatusId, UserStatusUpdateRequest request) {
        Instant newLastActiveAt = request.newLastActiveAt();

        UserStatus userStatus = userStatusRepository.findById(userStatusId)
                .orElseThrow(() -> new NoSuchElementException("UserStatus with id " + userStatusId + " not found"));

        userStatus.updateLastActiveAt(newLastActiveAt);

        return userStatus;
    }

    @Transactional
    @Override
    public UserStatusDto updateByUserId(UUID userId, UserStatusUpdateRequest request) {
        UserStatus userStatus = userStatusRepository.findByUser_Id(userId)
                .orElseThrow(() -> new NoSuchElementException("UserStatus with userId " + userId + " not found"));

        Instant newLastActiveAt = request.newLastActiveAt();
        userStatus.updateLastActiveAt(newLastActiveAt);

        return userStatusMapper.toDto(userStatus);
    }

    @Transactional
    @Override
    public void deleteUserStatus(UUID userStatusId) {
        UserStatus userStatus = userStatusRepository.findById(userStatusId)
                .orElseThrow(() -> new NoSuchElementException("UserStatus with id " + userStatusId + " not found"));
        userStatusRepository.delete(userStatus);
    }
}
