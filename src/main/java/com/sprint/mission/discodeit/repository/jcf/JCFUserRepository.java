package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

public class JCFUserRepository implements UserRepository {

    private static final Map<UUID, User> users = new HashMap<>();

    @Override
    public void save() { }

    @Override
    public void addUser(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public User findUserById(UUID userId) {
        return users.get(userId);
    }

    @Override
    public List<User> findUsersByIds(Set<UUID> userIds) {
        return users.values().stream()
                .filter(user -> userIds.contains(user.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findUserAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void deleteUserById(UUID userId) {
        users.remove(userId);
    }

    @Override
    public boolean existsById(UUID userId) {
        return users.containsKey(userId);
    }
}
