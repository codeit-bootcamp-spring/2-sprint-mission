package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

//@Repository
public class JCFUserStatusRepository implements UserStatusRepository {
    private final List<UserStatus> userStatusList = new ArrayList<>();

    @Override
    public void save(UserStatus userStatus) {
        userStatusList.add(userStatus);
    }

    @Override
    public Optional<UserStatus> findById(UUID userStatusUUID) {
        return userStatusList.stream()
                .filter(userStatus -> userStatus.getId().equals(userStatusUUID))
                .findAny();
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userUUID) {
        return userStatusList.stream()
                .filter(userStatus -> userStatus.getUserUUID().equals(userUUID))
                .findAny();
    }

    @Override
    public List<UserStatus> findAll() {
        return userStatusList;
    }

    @Override
    public void update(UUID userStatusUUID) {
        userStatusList.stream()
                .filter(userStatus -> userStatus.getId().equals(userStatusUUID))
                .findAny()
                .ifPresent(UserStatus::updateLastLoginTime);
    }

    @Override
    public void delete(UUID statusUUID) {
        userStatusList.removeIf(userStatus -> userStatus.getId().equals(statusUUID));
    }
}
