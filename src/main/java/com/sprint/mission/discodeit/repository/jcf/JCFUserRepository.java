package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@ConditionalOnProperty(
        name = "discordit.repository.type",
        havingValue = "jcf")
public class JCFUserRepository implements UserRepository {

    private static final Map<UUID, User> users = new HashMap<>();

    @Override
    public void save() {
    }

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
    public User findUserByName(String username) {
        return users.values().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElse(null);
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

    @Override
    public boolean existsByUsername(String username) {
        return users.values().stream()
                .anyMatch(user -> user.getUsername().equals(username));
    }

    @Override
    public boolean existsByEmail(String email) {
        return users.values().stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }
}
