package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.*;

public class JCFUserRepository implements UserRepository {
    private final Map<UUID, User> data = new HashMap<>();

    @Override
    public User save(User user) {
        data.put(user.getUuid(), user);
        return user;
    }

    @Override
    public User findByKey(UUID userKey) {
        return data.get(userKey);
    }

    @Override
    public boolean existsByKey(UUID userKey) {
        return data.containsKey(userKey);
    }

    @Override
    public void delete(User user) {
        data.remove(user.getUuid());
    }

    @Override
    public User findByUserId(String userId) {
        return data.values().stream()
                .filter(u -> u.getId().equals(userId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<User> findAllByIds(List<String> userIds) {
        return data.values().stream().filter(u -> userIds.contains(u.getId())).toList();
    }
}
