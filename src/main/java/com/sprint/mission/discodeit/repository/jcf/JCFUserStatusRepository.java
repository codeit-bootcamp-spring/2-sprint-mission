package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class JCFUserStatusRepository implements UserStatusRepository {
    List<UserStatus> userStatusList = new ArrayList<>();

    @Override
    public void save(UUID userUUID) {
        UserStatus userStatus = new UserStatus(userUUID);
        userStatusList.add(userStatus);
    }

    @Override
    public Optional<UserStatus> findByUser(UUID userUUID) {
        return userStatusList.stream()
                .filter(userStatus -> userStatus.getUserUUID().equals(userUUID)).findFirst();
    }

    @Override
    public List<UserStatus> findAll() {
        return userStatusList;
    }

    @Override
    public void update(UUID userUUID) {
        userStatusList.stream()
                .filter(userStatus -> userStatus.getUserUUID().equals(userUUID))
                .findAny()
                .ifPresent(UserStatus::updateLastStatus);
    }

    @Override
    public void delete(UUID userUUID) {
        userStatusList.removeIf(userStatus -> userStatus.getUserUUID().equals(userUUID));
    }
}
