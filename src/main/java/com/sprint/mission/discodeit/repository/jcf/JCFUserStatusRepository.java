package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
public class JCFUserStatusRepository implements UserStatusRepository {

    private final Map<UUID, UserStatus> data;

    public JCFUserStatusRepository() {
        this.data = new HashMap<>();
    }

    @Override
    public UserStatus save(UserStatus userStatus) {
        return data.put(userStatus.getId(), userStatus);
    }

    @Override
    public List<UserStatus> findAll() {
        return data.values().stream().toList();
    }

    @Override
    public UserStatus findById(UUID userStatusId) {
        return data.get(userStatusId);
    }

    @Override
    public UserStatus findByUserId(UUID userId) {
        return data.values().stream()
                .filter(userStatus ->
                        userStatus.getUserId().equals(userId)
                )
                .findFirst()
                .orElse(null);
    }

    @Override
    public void delete(UUID userStatusId) {
        data.remove(userStatusId);
    }
}
