package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusDto.UserStatusCreateRequest;
import com.sprint.mission.discodeit.service.UserStatusDto.UserStatusUpdateByUserRequest;
import com.sprint.mission.discodeit.service.UserStatusDto.UserStatusUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class UserStatusService {
    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    public UserStatus createUserStatus(UserStatusCreateRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (userStatusRepository.existsByUserId(request.userId())) {
            throw new IllegalStateException("UserStatus already exists for this user");
        }

        UserStatus userStatus = new UserStatus(request.userId(), request.status());
        return userStatusRepository.save(userStatus);
    }

    public UserStatus findById(UUID id) {
        return userStatusRepository.findByUserId(id)
                .orElseThrow(() -> new RuntimeException("UserStatus not found"));
    }

    public List<UserStatus> findAll() {
        return userStatusRepository.findAll();
    }

    public void update(UserStatusUpdateRequest request) {
        UserStatus userStatus = userStatusRepository.findByUserId(request.userStatusId())
                .orElseThrow(() -> new RuntimeException("UserStatus not found"));

        userStatus.update(request.status());
        userStatusRepository.save(userStatus);
    }

    public void updateByUserId(UserStatusUpdateByUserRequest request) {
        UserStatus userStatus = userStatusRepository.findByUserId(request.userId())
                .orElseThrow(() -> new RuntimeException("UserStatus not found for this user"));

        userStatus.update(request.status());
        userStatusRepository.save(userStatus);
    }

    public void deleteUserStatus(UUID id) {
        userStatusRepository.deleteByUserId(id);
    }
}