package com.sprint.mission.discodeit.Repository.jcf;

import com.sprint.mission.discodeit.DTO.UserStatus.UserStatusCRUDDTO;
import com.sprint.mission.discodeit.Exception.Empty.EmptyUserStatusListException;
import com.sprint.mission.discodeit.Exception.NotFound.UserStatusNotFoundException;
import com.sprint.mission.discodeit.Repository.UserStatusRepository;
import com.sprint.mission.discodeit.entity.UserStatus;
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
    public UserStatus find(UUID userId) {
        UserStatus status = userStatusList.stream().filter(userStatus -> userStatus.getUserId().equals(userId))
                .findFirst().orElseThrow(() -> new UserStatusNotFoundException("유저 상태를 찾지 못했습니다."));
        return status;
    }

    @Override
    public List<UserStatus> findAll() {
        if (userStatusList.isEmpty()) {
            throw new EmptyUserStatusListException("Repository 내 유저 상태 리스트가 비어있습니다.");
        }
        return userStatusList;
    }

    @Override
    public UserStatus update(UserStatus userStatus, UserStatusCRUDDTO userStatusUpdateDTO) {
        if (userStatusUpdateDTO.userStatusId() != null) {
            userStatus.setUserStatusId(userStatusUpdateDTO.userStatusId());
        }
        return userStatus;
    }

    @Override
    public void delete(UUID userId) {
        UserStatus userStatus = find(userId);
        userStatusList.remove(userStatus);
    }
}
