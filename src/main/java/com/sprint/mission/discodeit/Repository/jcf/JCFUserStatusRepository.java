package com.sprint.mission.discodeit.Repository.jcf;

import com.sprint.mission.discodeit.DTO.UserStatusCreateDTO;
import com.sprint.mission.discodeit.DTO.UserStatusUpdateDTO;
import com.sprint.mission.discodeit.Repository.UserStatusRepository;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class JCFUserStatusRepository implements UserStatusRepository {
    private Map<UUID, UserStatus> userStatusMap = new ConcurrentHashMap<>();

    @Override
    public void save(UserStatusCreateDTO userStatusCreateDTO) {
        userStatusMap.put(userStatusCreateDTO.userId(), userStatusCreateDTO.userStatus());
    }

    @Override
    public UserStatus find(UUID userId) {
        UserStatus userStatus = userStatusMap.get(userId);
        return userStatus;
    }

    @Override
    public List<UserStatus> findAll() {
        return new ArrayList<>(userStatusMap.values());
    }

    @Override
    public void update(UserStatusUpdateDTO userStatusUpdateDTO) {
        UserStatus userStatus = find(userStatusUpdateDTO.userId());
        userStatus.setUserStatusId(userStatusUpdateDTO.replaceId());
    }

    @Override
    public void delete(UUID userId) {
        userStatusMap.remove(userId);
    }
}
