package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.CreateUserStatusRequest;
import com.sprint.mission.discodeit.dto.UpdateUserStatusRequest;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;

    @Override
    public UserStatus create(CreateUserStatusRequest request) {
        UUID userId = request.userId();

        if (userRepository.getUserById(userId).isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 사용자 입니다: " + userId);
        }

        if (userStatusRepository.getById(userId).isPresent()) {
            throw new IllegalArgumentException("이미 정보가 존재하는 사용자입니다: " + userId);
        }

        UserStatus userStatus = new UserStatus(userId);
        userStatusRepository.save(userStatus);
        return userStatus;
    }

    @Override
    public Optional<UserStatus> getById(UUID userId) {
        return userStatusRepository.getById(userId);
    }

    @Override
    public List<UserStatus> getAll() {
        return userStatusRepository.getAll();
    }

    @Override
    public void update(UpdateUserStatusRequest request) {
        userStatusRepository.getById(request.userId()).ifPresent(userStatus -> {
            userStatus.updateLastAccessAt(request.lastAccessAt());
            userStatusRepository.save(userStatus);
        });
    }

    @Override
    public void updateByUserId(UUID userId) {
        userStatusRepository.getById(userId).ifPresent(userStatus -> {
            userStatus.updateLastAccessAt(Instant.now());
            userStatusRepository.save(userStatus);
        });
    }

    @Override
    public void delete(UUID userId) {
        userStatusRepository.deleteById(userId);
    }
}
