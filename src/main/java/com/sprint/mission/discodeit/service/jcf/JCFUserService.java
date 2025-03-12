package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {
    private static volatile JCFUserService instance;

    private final Map<UUID, User> data;

    private JCFUserService() {
        this.data = new HashMap<>();
    }

    public static JCFUserService getInstance() {
        if(instance == null) {
            synchronized (JCFUserService.class) {
                if(instance == null) {
                    instance = new JCFUserService();
                }
            }
        }
        return instance;
    }

    @Override
    public void create(User user) {
        if(user == null) {
            throw new IllegalArgumentException("user 객체가 null 입니다.");
        }
        data.put(user.getId(), user);
    }

    @Override
    public User findById(UUID userId) {
        return Optional.ofNullable(data.get(userId))
                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void delete(UUID userId) {
        User user = findById(userId);
        data.remove(user.getId());
    }

    @Override
    public void update(UUID userId, String nickname, String email, String description) {
        User user = findById(userId);
        user.update(nickname, email, description, System.currentTimeMillis());
    }
}
