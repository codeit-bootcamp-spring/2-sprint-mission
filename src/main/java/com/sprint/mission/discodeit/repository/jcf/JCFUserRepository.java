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
    public List<User> findAllByKeys(List<UUID> userKeys) {
        return userKeys.stream().map(data::get).toList();
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
    public String findUserName(UUID userKey) {
        return data.get(userKey).getName();
    }

    @Override
    public String findUserId(UUID userKey) {
        return data.get(userKey).getId();
    }

    @Override
    public UUID findUserKeyById(String userId) {
        return data.values().stream()
                .filter(u -> u.getId().equals(userId))
                .map(User::getUuid)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<UUID> findUserKeyByIds(List<String> userIds) {
        return data.values().stream()
                .filter(u -> userIds.contains(u.getId()))
                .map(User::getUuid)
                .toList();
    }

}
