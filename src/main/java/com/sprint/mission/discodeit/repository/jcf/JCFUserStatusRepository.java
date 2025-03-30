package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.status.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
public class JCFUserStatusRepository implements UserStatusRepository {
    private final Map<UUID,UserStatus> userStatusData = new HashMap<>();


    @Override
    public UserStatus save(UserStatus userStatus) {
        userStatusData.put(userStatus.getId(), userStatus);
        return userStatus;
    }

    @Override
    public UserStatus findByUserId(UUID id) {
        return userStatusData.values().stream().
                filter(userStatus ->
                        userStatus.getUserId().equals(id)).
                findFirst().
                orElse(null);
    }

    @Override
    public UserStatus findById(UUID id) {
        return userStatusData.get(id);
    }

    @Override
    public List<UserStatus> findAll() {
        return userStatusData.values().stream().toList();
    }


    @Override
    public UserStatus update(UUID id) {
        UserStatus userStatusNullable = userStatusData.get(id);
        UserStatus userStatus = Optional.ofNullable(userStatusNullable).orElseThrow(() -> new NoSuchElementException(id + "가 존재하지 않습니다."));
        userStatus.updateLastLogin();

        return userStatus;
    }

    @Override
    public void delete(UUID id) {
        if(!userStatusData.containsKey(id)){
            throw new NoSuchElementException("유저 " + id + "가 존재하지 않습니다.");
        }
        userStatusData.remove(id);
    }
}
