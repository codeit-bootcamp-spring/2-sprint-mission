package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.status.CreateUserStatusRequest;
import com.sprint.mission.discodeit.dto.status.UpdateUserStatusRequest;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.handler.UserStatusNotFoundException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
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

        try {
            userStatusRepository.getById(userId);
            throw new IllegalArgumentException("이미 정보가 존재하는 사용자 입니다: " + userId);
        } catch (UserStatusNotFoundException e) {
            // 존재하지 않으면 그대로 진행
        }

        UserStatus userStatus = new UserStatus(userId);
        userStatusRepository.save(userStatus);
        return userStatus;
    }

    @Override
    public UserStatus getById(UUID userId) {
        return userStatusRepository.getById(userId);
    }

    @Override
    public List<UserStatus> getAll() {
        return userStatusRepository.getAll();
    }

    @Override
    public void update(UpdateUserStatusRequest request) {
        UserStatus userStatus = userStatusRepository.getById(request.userId());
        userStatus.updateLastAccessAt(request.lastAccessAt());
        userStatusRepository.save(userStatus);
    }

    @Override
    public void updateByUserId(UUID userId) {
        UserStatus userStatus = userStatusRepository.getById(userId);
        userStatus.updateLastAccessAt(Instant.now());
        userStatusRepository.save(userStatus);
    }

    @Override
    public void delete(UUID userId) {
        userStatusRepository.deleteById(userId);
    }
}
