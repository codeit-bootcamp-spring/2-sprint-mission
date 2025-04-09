package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.status.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public class JCFUserStatusRepository implements UserStatusRepository {

    Map<UUID, UserStatus> store = new HashMap<>();

    @Override
    public void save(UserStatus userStatus) {
        store.put(userStatus.getUserId(), userStatus);
    }

    @Override
    public UserStatus findByUserStatusId(UUID userStatusId) {
        return store.get(userStatusId);
    }

    @Override
    public UserStatus findByUserId(UUID userId) {
        return findAll().stream()
            .filter(us -> us.getUserId().equals(userId))
            .findFirst().orElse(null);
    }

    @Override
    public List<UserStatus> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void delete(UUID userStatusId) {
        store.remove(userStatusId);
    }

    @Override
    public void deleteByUserId(UUID userId) {
        findAll().stream()
            .filter(id -> id.getUserId().equals(userId)).findAny()
            .ifPresent(userStatus -> delete(userStatus.getId()));
    }
}
