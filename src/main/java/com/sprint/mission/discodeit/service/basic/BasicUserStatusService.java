package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.status.CreateUserStatusRequest;
import com.sprint.mission.discodeit.dto.status.UpdateUserStatusRequest;
import com.sprint.mission.discodeit.entity.User;
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
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자 입니다: " + userId));

        if (userRepository.findById(userId).isPresent()) {
            throw new IllegalArgumentException("이미 정보가 존재하는 사용자 입니다. " + userId);
        }

        UserStatus userStatus = new UserStatus(user);
        userStatusRepository.save(userStatus);
        return userStatus;
    }

    @Override
    @Transactional(readOnly = true)
    public UserStatus getById(UUID userId) {
        return userStatusRepository.findById(userId)
            .orElseThrow(() -> new UserStatusNotFoundException(userId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserStatus> getAll() {
        return userStatusRepository.findAll();
    }

    @Override
    @Transactional
    public void update(UpdateUserStatusRequest request) {
        UserStatus userStatus = userStatusRepository.findById(request.userId())
            .orElseThrow(
                () -> new UserStatusNotFoundException(request.userId()));
        userStatus.updateLastActiveAt(request.newLastActiveAt());
//        userStatusRepository.save(userStatus);
    }

    @Override
    @Transactional
    public void updateByUserId(UUID userId) {
        UserStatus userStatus = userStatusRepository.findById(userId)
            .orElseThrow(() -> new UserStatusNotFoundException(userId));

        userStatus.updateLastActiveAt(Instant.now());
//        userStatusRepository.save(userStatus);
    }

    @Override
    @Transactional
    public void delete(UUID userId) {
        userStatusRepository.deleteById(userId);
    }
}
