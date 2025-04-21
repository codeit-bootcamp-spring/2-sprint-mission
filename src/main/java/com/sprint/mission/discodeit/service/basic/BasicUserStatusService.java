package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userstatus.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusResult;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public UserStatusResult create(UserStatusCreateRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new EntityNotFoundException("해당 유저가 존재하지 않습니다."));

        userStatusRepository.findByUser_Id(request.userId())
                .ifPresent(status -> {
                    throw new IllegalArgumentException("해당 유저의 상태가 이미 존재합니다.");
                });

        UserStatus userStatus = userStatusRepository.save(
                new UserStatus(user, request.lastActiveAt()));

        return UserStatusResult.fromEntity(userStatus,
                userStatus.isOnline(ZonedDateTime.now().toInstant()));
    }

    @Transactional(readOnly = true)
    @Override
    public UserStatusResult getByUserId(UUID userId) {
        UserStatus userStatus = userStatusRepository.findByUser_Id(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당 유저 Id를 가진 UserStatus가 없습니다."));

        return UserStatusResult.fromEntity(userStatus, userStatus.isOnline(Instant.now()));
    }

    @Transactional
    @Override
    public UserStatusResult updateByUserId(UUID userId, UserStatusUpdateRequest userStatusUpdateRequest) {
        UserStatus userStatus = userStatusRepository.findByUser_Id(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당 유저Id를 가진 UserStatus가 없습니다."));

        userStatus.updateLastActiveAt(userStatusUpdateRequest.newLastActiveAt());
        UserStatus updatedUserStatus = userStatusRepository.save(userStatus);

        return UserStatusResult.fromEntity(updatedUserStatus, updatedUserStatus.isOnline(Instant.now()));
    }

    @Transactional
    @Override
    public void delete(UUID userStatusId) {
        userStatusRepository.deleteById(userStatusId);
    }
}
