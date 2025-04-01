package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.entity.UserStatusType;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.service.dto.user.userstatus.UserStatusCreateRequest;
import com.sprint.mission.discodeit.service.dto.user.userstatus.UserStatusUpdateRequest;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {
    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    @Override
    public UserStatus create(UserStatusCreateRequest statusParam) {
        if (!userRepository.existsById(statusParam.userId())) {
            throw new IllegalArgumentException(statusParam.userId() + " 에 해당하는 사용자를 찾을 수 없음");
        }

        if (userStatusRepository.existsByUserId(statusParam.userId())) {
            throw new IllegalArgumentException(statusParam.userId() + " 에 해당하는 UserStatus를 이미 존재함");
        }
        UserStatus userStatus = new UserStatus(statusParam.userId());
        return userStatusRepository.save(userStatus);
    }

    @Override
    public UserStatus find(UUID id) {
        return userStatusRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(id + " 에 해당하는 UserStatus를 찾을 수 없음"));
    }

    @Override
    public List<UserStatus> findAll() {
        return userStatusRepository.findAll();
    }

    @Override
    public UserStatus findByUserId(UUID userId) {
        return userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException(userId + " 에 해당하는 UserStatus를 찾을 수 없음"));
    }

    @Override
    public UserStatus update(UUID userId, UserStatusUpdateRequest statusParam) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException(userId + "에 해당하는 User를 찾을 수 없음");
        }
        UserStatus userStatus = findByUserId(userId);
        userStatus.update(UserStatusType.fromString(statusParam.status()));

        return userStatusRepository.save(userStatus);
    }

    @Override
    public UserStatus updateByUserId(UUID userId) {
        UserStatus userStatus = findByUserId(userId);
        if (userStatus.getStatus() == UserStatusType.OFFLINE) {
            userStatus.update(UserStatusType.ONLINE);
        }
        return userStatusRepository.save(userStatus);
    }

    @Override
    public void delete(UUID id) {
        if (!userStatusRepository.existsById(id)) {
            throw new IllegalArgumentException(id + " 에 해당하는 UserStatus를 찾을 수 없음");
        }
        userStatusRepository.deleteById(id);
    }
}
