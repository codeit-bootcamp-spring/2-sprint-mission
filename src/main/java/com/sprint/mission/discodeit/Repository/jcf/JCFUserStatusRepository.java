package com.sprint.mission.discodeit.Repository.jcf;

import com.sprint.mission.discodeit.DTO.UserStatusUpdateDTO;
import com.sprint.mission.discodeit.Exception.UserStatusNotFoundException;
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
                .findFirst().orElseThrow(() -> new UserStatusNotFoundException("해당 ID를 가지는 User Status 를 찾을 수 없습니다."));
        return status;
    }

    @Override
    public List<UserStatus> findAll() {
        return userStatusList;
    }

    @Override
    public void update(UserStatusUpdateDTO userStatusUpdateDTO) {
        UserStatus userStatus = find(userStatusUpdateDTO.userId());
        userStatus.setUserStatusId(userStatusUpdateDTO.replaceId());
    }

    @Override
    public void delete(UUID userId) {
        UserStatus userStatus = find(userId);
        userStatusList.remove(userStatus);
    }
}
