package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userStatus.CreateUserStatusDTO;
import com.sprint.mission.discodeit.dto.userStatus.UpdateUserStatusDTO;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        if (!userRepository.existsById(dto.userId())) {
            throw new NoSuchElementException("존재하지 않는 사용자입니다.");
        }

        if (userStatusRepository.existsByUserId(dto.userId())) {
            throw new IllegalStateException("이미 존재하는 사용자 상태입니다.");
        }

        UserStatus userStatus = new UserStatus(dto.userId(), dto.lastActiveAt());
        return userStatusRepository.saveUserStatus(userStatus);
    }

    @Override
    public UserStatus findById(UUID userId) {
        return userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 상태 ID입니다."));
    }

    @Override
    public List<UserStatus> findAll() {
        return userStatusRepository.findAll();
    }

    @Override
    public UserStatus updateUserStatus(UpdateUserStatusDTO dto) {
        UserStatus userStatus = findById(dto.userId());

        userStatus.updateLastActiveAt(dto.lastActiveAt());
        return userStatusRepository.saveUserStatus(userStatus);
    }

    @Override
    public UserStatus updateByUserId(UUID userId, UpdateUserStatusDTO dto) {
        UserStatus userStatus = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("해당 사용자의 상태가 없습니다."));

        userStatus.updateLastActiveAt(dto.lastActiveAt());
        return userStatusRepository.saveUserStatus(userStatus);
    }

    @Override
    public void deleteUserStatus(UUID userId) {
        if (!userStatusRepository.existsByUserId(userId)) {
            throw new NoSuchElementException("존재하지 않는 상태 ID입니다.");
        }
        userStatusRepository.delete(userId);
    }
}
