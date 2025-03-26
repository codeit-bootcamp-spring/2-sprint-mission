package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userStatus.CreateUserStatusDTO;
import com.sprint.mission.discodeit.dto.userStatus.UpdateUserStatusDTO;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {
    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    @Override
    public UserStatus createUserStatus(CreateUserStatusDTO dto) {
        UUID userId = dto.userId();
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("존재하지 않는 사용자입니다.");
        }

        if (userStatusRepository.findByUserId(userId).isPresent()) {
            throw new IllegalStateException("이미 존재하는 사용자 상태입니다.");
        }
        Instant lastActiveAt = dto.lastActiveAt();
        UserStatus userStatus = new UserStatus(userId, lastActiveAt);
        return userStatusRepository.saveUserStatus(userStatus);
    }

    @Override
    public UserStatus findById(UUID userStatusId) {
        return userStatusRepository.findById(userStatusId)
                .orElseThrow(() -> new NoSuchElementException("UserStatus with id " + userStatusId + " not found"));
    }

    @Override
    public List<UserStatus> findAll() {
        return userStatusRepository.findAll().stream()
                .toList();
    }

    @Override
    public UserStatus updateUserStatus(UUID userStatusId, UpdateUserStatusDTO dto) {
        Instant newLastActiveAt = dto.lastActiveAt();

        UserStatus userStatus = userStatusRepository.findById(userStatusId)
                .orElseThrow(() -> new NoSuchElementException("UserStatus with id " + userStatusId + " not found)"));

        userStatus.updateLastActiveAt(newLastActiveAt);

        return userStatusRepository.saveUserStatus(userStatus);
    }

    @Override
    public UserStatus updateByUserId(UUID userId, UpdateUserStatusDTO dto) {
        Instant newLastActiveAt = dto.lastActiveAt();

        UserStatus userStatus = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("UserStatus with userId \" + userId + \" not found"));

        userStatus.updateLastActiveAt(newLastActiveAt);

        return userStatusRepository.saveUserStatus(userStatus);
    }

    @Override
    public void deleteUserStatus(UUID userStatusId) {
        if (!userStatusRepository.existsById(userStatusId)) {
            throw new NoSuchElementException("UserStatus with id " + userStatusId + " not found");
        }
        userStatusRepository.deleteById(userStatusId);
    }
}
