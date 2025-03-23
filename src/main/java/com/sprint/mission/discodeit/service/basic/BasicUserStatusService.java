package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.service.dto.user.userstatus.UserStatusParam;
import java.time.Instant;
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
    public void create(UserStatusParam statusparam) {
        if (!userRepository.existsById(statusparam.userId())) {
            throw new IllegalArgumentException(statusparam.userId() + " 에 해당하는 사용자를 찾을 수 없음");
        }

        if (userStatusRepository.existsByUserId(statusparam.userId())) {
            throw new IllegalStateException(statusparam.userId() + " 에 해당하는 userStatus가 이미 존재함");
        }
        UserStatus userStatus = new UserStatus(statusparam.userId(), statusparam.time());
        userStatusRepository.save(userStatus);
    }

    @Override
    public UserStatus find(UUID id) {
        return userStatusRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(id + " 에 해당하는 userStatus를 찾을 수 없음"));
    }

    @Override
    public List<UserStatus> findAll() {
        return userStatusRepository.findAll();
    }

    @Override
    public UserStatus findByUserId(UUID userId) {
        return userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException(userId + " 에 해당하는 userStatus를 찾을 수 없음"));
    }

    @Override
    public UserStatus update(UserStatusParam statusparam) {
        UserStatus userStatus = findByUserId(statusparam.userId());
        userStatus.update(statusparam.time());
        return userStatusRepository.save(userStatus);
    }

    @Override
    public UserStatus updateByUserId(UUID userId) {
        UserStatus userStatus = findByUserId(userId);
        Instant now = Instant.now();
        userStatus.update(now);
        return userStatusRepository.save(userStatus);
    }

    @Override
    public void delete(UUID id) {
        userStatusRepository.deleteById(id);
    }
}
