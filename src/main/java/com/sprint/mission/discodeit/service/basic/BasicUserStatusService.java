package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.entity.UserStatusType;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.service.dto.UserStatusParam;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class BasicUserStatusService implements UserStatusService {
    private UserStatusRepository userStatusRepository;
    private UserRepository userRepository;

    @Override
    public void create(UserStatusParam statusparam) {
        if (!userRepository.existsById(statusparam.userId())) {
            throw new NoSuchElementException(statusparam.userId() + " 에 해당하는 사용자를 찾을 수 없음");
        }

        if (userStatusRepository.existsByUserId(statusparam.userId())) {
            throw new IllegalStateException(statusparam.userId() + " 에 해당하는 userStatus가 이미 존재함");
        }
        UserStatus userStatus = new UserStatus(statusparam.userId(), statusparam.status());
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
        UUID userId = statusparam.userId();
        UserStatus userStatus = findByUserId(statusparam.userId());
        userStatus.update(statusparam.status());
        return userStatusRepository.save(userStatus);
    }

    @Override
    public UserStatus updateByUserId(UUID userId) {
        UserStatus userStatus = findByUserId(userId);
        UserStatusType status;
        Instant now = Instant.now();
        if (Duration.between(userStatus.getUpdatedAt(), now).toMinutes() > 5) {
            status = UserStatusType.OFFLINE;
        } else {
            status = UserStatusType.ONLINE;
        }
        userStatus.update(status);
        return userStatusRepository.save(userStatus);
    }

    // 삭제하려는 userId가진 UserStatus조회 -> 있으면 해당 UserStatus 삭제 -> User객체 삭제
    @Override
    public void delete(UUID id) {
        if (!userStatusRepository.existsById(id)) {
            throw new NoSuchElementException(id + " 에 해당하는 userStatus를 찾을 수 없음");
        }
        userRepository.deleteById(id);
    }
}
