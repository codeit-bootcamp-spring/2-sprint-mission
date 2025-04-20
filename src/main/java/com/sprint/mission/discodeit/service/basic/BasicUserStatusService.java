package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.application.dto.userstatus.UserStatusCreateRequest;
import com.sprint.mission.discodeit.application.dto.userstatus.UserStatusResult;
import com.sprint.mission.discodeit.application.dto.userstatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    @Override
    public UserStatusResult create(UserStatusCreateRequest request) {
        User user = userRepository.findByUserId(request.userId())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        userStatusRepository.findByUserId(request.userId())
                .ifPresent(status -> {
                    throw new IllegalArgumentException("해당 유저의 상태가 이미 존재합니다.");
                });

        UserStatus userStatus = userStatusRepository.save(
                new UserStatus(user, request.lastActiveAt()));

        return UserStatusResult.fromEntity(userStatus,
                userStatus.isOnline(ZonedDateTime.now().toInstant()));
    }

    @Override
    public UserStatusResult getByUserId(UUID userId) {
        UserStatus userStatus = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저 Id를 가진 UserStatus가 없습니다."));

        return UserStatusResult.fromEntity(userStatus, userStatus.isOnline(Instant.now()));
    }

    @Override
    public UserStatusResult updateByUserId(UUID userId, UserStatusUpdateRequest userStatusUpdateRequest) {
        UserStatus userStatus = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저Id를 가진 UserStatus가 없습니다."));

        userStatus.updateLastActiveAt(userStatusUpdateRequest.newLastActiveAt());
        UserStatus updatedUserStatus = userStatusRepository.save(userStatus);

        return UserStatusResult.fromEntity(updatedUserStatus, updatedUserStatus.isOnline(Instant.now()));
    }

    @Override
    public void delete(UUID userStatusId) {
        userStatusRepository.delete(userStatusId);
    }
}
