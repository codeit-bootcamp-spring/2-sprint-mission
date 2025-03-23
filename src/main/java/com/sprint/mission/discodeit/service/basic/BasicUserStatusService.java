package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.application.dto.userStatus.UserStatusDto;
import com.sprint.mission.discodeit.application.dto.userStatus.UserStatusesDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {
    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    @Override
    public UserStatusDto create(UUID userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        userStatusRepository.findByUserId(userId)
                .ifPresent(status -> {
                    throw new IllegalArgumentException("해당 유저의 상태가 이미 존재합니다.");
                });

        UserStatus userStatus = userStatusRepository.save(new UserStatus(userId));

        return UserStatusDto.fromEntity(userStatus);
    }

    @Override
    public UserStatusDto find(UUID userStatusId) {
        UserStatus userStatus = userStatusRepository.findById(userStatusId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        return UserStatusDto.fromEntity(userStatus);
    }

    @Override
    public UserStatusDto findByUserId(UUID userId) {
        UserStatus userStatus = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저 Id를 가진 UserStatus가 없습니다."));

        return UserStatusDto.fromEntity(userStatus);
    }

    @Override
    public UserStatusesDto findAll() {
        List<UserStatus> userStatuses = userStatusRepository.findAll();

        return UserStatusesDto.fromEntity(userStatuses);
    }

    @Override
    public UserStatusDto update(UUID userStatusId) {
        UserStatus update = userStatusRepository.update(userStatusId);

        return UserStatusDto.fromEntity(update);
    }

    @Override
    public UserStatusDto updateByUserId(UUID userId) {
        UserStatus userStatus = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저 Id를 가진 UserStatus가 없습니다."));

        userStatus.updateLastLoginAt();
        UserStatus updatedUserStatus = userStatusRepository.save(userStatus);

        return UserStatusDto.fromEntity(updatedUserStatus);
    }

    @Override
    public void delete(UUID userStatusId) {
        userStatusRepository.delete(userStatusId);
    }
}
