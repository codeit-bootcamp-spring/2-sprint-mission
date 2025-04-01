package com.sprint.mission.discodeit.adapter.outbound.user;

import com.sprint.mission.discodeit.core.user.entity.UserStatus;
import com.sprint.mission.discodeit.core.user.port.UserStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
@Repository
public class JCFUserStatusRepository implements UserStatusRepository {
    List<UserStatus> userStatusList = new ArrayList<>();

    @Override
    public UserStatus save(UserStatus userStatus) {
        userStatusList.add(userStatus);
        return userStatus;
    }

    @Override
    public UserStatus findByUserId(UUID userId) {
        UserStatus status = userStatusList.stream().filter(userStatus -> userStatus.getUserId().equals(userId))
                .findFirst().orElse(null);
        return status;
    }

    @Override
    public UserStatus findByStatusId(UUID userStatusId) {
        UserStatus status = userStatusList.stream().filter(userStatus -> userStatus.getUserStatusId().equals(userStatusId))
                .findFirst().orElse(null);
        return status;
    }

    @Override
    public List<UserStatus> findAll() {
        return userStatusList;
    }

    @Override
    public UserStatus update(UserStatus userStatus) {
        userStatus.updatedTime();
        return userStatus;
    }

    @Override
    public void deleteById(UUID userStatusId) {
        UserStatus userStatus = findByStatusId(userStatusId);
        userStatusList.remove(userStatus);
    }

    @Override
    public void deleteByUserId(UUID userId) {
        UserStatus userStatus = findByUserId(userId);
        userStatusList.remove(userStatus);
    }
}
