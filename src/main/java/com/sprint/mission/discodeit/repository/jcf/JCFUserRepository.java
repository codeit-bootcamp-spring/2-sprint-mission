package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.*;

public class JCFUserRepository implements UserRepository {
    private static volatile JCFUserRepository instance;
    private final Map<UUID, User> data;

    public static JCFUserRepository getInstance() {
        if (instance == null) {
            synchronized (JCFUserRepository.class) {
                if (instance == null) {
                    instance = new JCFUserRepository();
                }
            }
        }
        return instance;
    }

    private JCFUserRepository() {
        this.data = new HashMap<>();
    }

    @Override
    public User save(User user) {
        data.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(UUID userId) {
        return Optional.ofNullable(data.get(userId));
    }

    @Override
    public List<User> findAll() {
        return data.values().stream().toList();
    }


    @Override
    public void deleteById(UUID userId) {
        data.remove(userId);
    }
}
