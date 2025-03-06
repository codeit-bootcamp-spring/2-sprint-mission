package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCFUserRepository implements UserRepository {
    private final Map<UUID, User> users = new HashMap<>();

    @Override
    public User save(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User findById(UUID id) {
        return users.get(id);
    }

    @Override
    public List<User> findByName(String name) {
        return users.values()
                .stream()
                .filter(user -> user.isSameName(name))
                .toList();
    }

    @Override
    public User findByEmail(String email) {
        return users.values()
                .stream()
                .filter(user -> user.isSameEmail(email))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<User> findAll() {
        return users.values()
                .stream()
                .toList();
    }

    @Override
    public void updateName(UUID id, String name) {
        User user = users.get(findById(id).getId());
        user.updateName(name);
    }

    @Override
    public void delete(UUID id) {
        users.remove(id);
    }
}
