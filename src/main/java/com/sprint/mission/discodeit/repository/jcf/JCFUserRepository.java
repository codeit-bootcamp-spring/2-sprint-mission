package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.*;

public class JCFUserRepository implements UserRepository {
    private static final JCFUserRepository instance = new JCFUserRepository();
    private final Map<UUID, User> data = new HashMap<>();

    private JCFUserRepository() {}

    public static JCFUserRepository getInstance() {
        return instance;
    }

    @Override
    public void save(User user) {
        data.put(user.getId(), user);
    }

    @Override
    public Optional<User> getUserById(UUID userId) {
        return Optional.ofNullable(data.get(userId));
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void deleteUser(UUID userId) {
        data.remove(userId);
    }
}
