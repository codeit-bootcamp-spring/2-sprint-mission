package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.*;

public class JCFUserRepository implements UserRepository {
    private static volatile JCFUserRepository instance;

    private final Map<UUID, User> users;

    private JCFUserRepository() {
        users = new HashMap<>();
    }

    public static JCFUserRepository getInstance() {
        if (instance == null) {
            synchronized (JCFUserRepository.class) {
                if(instance == null) {
                    instance = new JCFUserRepository();
                }
            }
        }
        return instance;
    }

    @Override
    public User save(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User findById(UUID userId) {
        return users.get(userId);
    }

    @Override
    public User findByEmail(String email) {
        return users.values().stream()
                .filter(u -> email.equals(u.getEmail()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void delete(UUID userId) {
        users.remove(userId);
    }

    @Override
    public boolean exists(UUID userId) {
        return users.containsKey(userId);
    }
}
