package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class JCFUserRepository implements UserRepository {

    private final Map<UUID, User> userList = new HashMap<>();

    @Override
    public User save(User user) {
        userList.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findUserById(UUID userUUID) {
        return Optional.of(userList.get(userUUID));
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        return userList.values().stream()
                .filter(user -> user.getUsername().equals(username))
                .findAny();
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userList.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findAny();
    }

    @Override
    public List<User> findAllUser() {
        return userList.values().stream().toList();
    }

    @Override
    public void delete(UUID userUUID) {
        userList.remove(userUUID);
    }
}
