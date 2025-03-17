package com.sprint.mission.discodeit.Repository.jcf;

import com.sprint.mission.discodeit.DTO.UserStatusUpdateDTO;
import com.sprint.mission.discodeit.Exception.CommonExceptions;
import com.sprint.mission.discodeit.Repository.UserStatusRepository;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFUserStatusRepository implements UserStatusRepository {
    List<UserStatus> userStatusList = new ArrayList<>();

    @Override
    public void save(UserStatus userStatus) {
        userStatusList.add(userStatus);
    }

    @Override
    public UserStatus find(UUID userId) {
        UserStatus status = userStatusList.stream().filter(userStatus -> userStatus.getUserId().equals(userId))
                .findFirst().orElseThrow(() -> CommonExceptions.USER_STATUS_NOT_FOUND);
        return status;
    }

    @Override
    public List<UserStatus> findAll() {
        if (userStatusList.isEmpty()) {
            throw CommonExceptions.EMPTY_USER_STATUS_LIST;
        }
        return userStatusList;
    }

    @Override
    public void update(UserStatus userStatus, UserStatusUpdateDTO userStatusUpdateDTO) {
        if (userStatusUpdateDTO.replaceId() != null) {
            userStatus.setUserStatusId(userStatusUpdateDTO.replaceId());
        }
    }

    @Override
    public void delete(UUID userId) {
        UserStatus userStatus = find(userId);
        userStatusList.remove(userStatus);
    }
}
