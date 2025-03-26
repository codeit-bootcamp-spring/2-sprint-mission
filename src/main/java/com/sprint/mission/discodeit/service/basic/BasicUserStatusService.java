package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.CreateUserStatusDto;
import com.sprint.mission.discodeit.dto.UpdateUserStatusDto;
import com.sprint.mission.discodeit.entity.UserStatus;
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
    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    @Override
    public UserStatus create(CreateUserStatusDto dto) {
        if (userRepository.findByKey(dto.userKey()) == null) {
            throw new IllegalArgumentException("[Error] user is null");
        }
        if (userStatusRepository.findByUserKey(dto.userKey()) != null) {
            throw new IllegalArgumentException("[Error] userStatus is already exists");
        }
        UserStatus userStatus= new UserStatus(dto.userKey(), Instant.now());
        return userStatusRepository.save(userStatus);
    }

    @Override
    public UserStatus find(UUID userStatusKey) {
        UserStatus userStatus = userStatusRepository.findByKey(userStatusKey);
        if (userStatus == null) {
            throw new IllegalArgumentException("[Error] userStatus is null");
        }
        return userStatus;
    }

    @Override
    public List<UserStatus> findAll() {
        List<UserStatus> userStatuses = userStatusRepository.findAll();
        if (userStatuses == null) {
            throw new IllegalArgumentException("[Error] userStatus is null");
        }
        return userStatuses;
    }

    @Override
    public UserStatus update(UpdateUserStatusDto dto) {
        UserStatus userStatus = userStatusRepository.findByKey(dto.userStatusKey());
        if (userStatus == null) {
            throw new IllegalArgumentException("[Error] userStatus is null");
        }
        if (dto.lastActiveAt() == null) {
            throw new IllegalArgumentException("[Error] lastActiveAt is null");
        }
        userStatus.updateLastActiveAt(Instant.now());
        return userStatusRepository.save(userStatus);
    }

    @Override
    public void delete(UUID userStatusKey) {
        UserStatus userStatus = userStatusRepository.findByKey(userStatusKey);
        if (userStatus == null) {
            throw new IllegalArgumentException("[Error] userStatus is null");
        }
        userStatusRepository.delete(userStatusKey);
    }

    @Override
    public void deleteByUserKey(UUID userKey) {
        UserStatus userStatus = userStatusRepository.findByUserKey(userKey);
        if (userStatus == null) {
            throw new IllegalArgumentException("[Error] userStatus is null");
        }

        userStatusRepository.delete(userStatus.getUuid());
    }
}
