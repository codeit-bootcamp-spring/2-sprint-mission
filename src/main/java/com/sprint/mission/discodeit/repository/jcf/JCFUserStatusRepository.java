package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;

import java.util.*;

public class JCFUserStatusRepository implements UserStatusRepository {
    private static final Map<UUID, UserStatus> userStatusMap = new HashMap<>();

    @Override
    public void save() {
    }

    @Override
    public void addUserStatus(UserStatus userStatus) {
        userStatusMap.put(userStatus.getId(), userStatus);
    }

    @Override
    public UserStatus findUserStatusById(UUID userId) {
        return userStatusMap.get(userId);
    }

    @Override
    public List<UserStatus> findAllUserStatus() {
        return new ArrayList<>(userStatusMap.values());
    }

    @Override
    public void deleteUserStatusById(UUID userId) {
        userStatusMap.remove(userId);
    }

    @Override
    public boolean existsUserStatusById(UUID userId) {
        return userStatusMap.containsKey(userId);
    }
}

