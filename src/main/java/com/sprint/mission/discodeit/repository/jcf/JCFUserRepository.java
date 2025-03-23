package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.*;

public class JCFUserRepository implements UserRepository {
    private final HashMap<UUID, User> users = new HashMap<>();

    @Override
    public void save(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public Optional<List<User>> findAll() {
        return Optional.of(new ArrayList<>(users.values()));
    }

    @Override
    public void update(User user) {
        save(user);
    }

    @Override
    public void delete(UUID id) {
        users.remove(id);
    }

    @Override
    public Optional<User> findByUserName(String userName) {
        return users.values()
                .stream()
                .filter( user -> user.getUsername().equals(userName))
                .findFirst();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return users.values()
                .stream()
                .filter( user -> user.getEmail().equals(email))
                .findFirst();
    }
}
