package com.sprint.mission.discodeit.infra.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.infra.UserRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCFUserRepository implements UserRepository {
    private static final Map<UUID, User> users = new HashMap<>();

    @Override
    public User save(User user) {
        users.put(user.getId(), user);
        return findById(user.getId());
    }

    @Override
    public User findById(UUID id) {
        User user = users.get(id);
        if (user == null) {
            throw new IllegalArgumentException("[Error] 해당 유저가 없습니다");
        }

        return user;
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
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] Email과 일치하는 유저가 없습니다."));
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
