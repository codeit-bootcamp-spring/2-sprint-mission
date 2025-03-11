package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public class JCFUserService implements UserService {
    private volatile static JCFUserService instance = null;
    private final Map<UUID, User> data;

    private JCFUserService() {
        this.data = new HashMap<>();
    }

    public static JCFUserService getInstance() {
        if (instance == null) {
            synchronized (JCFUserService.class) {
                if (instance == null) {
                    instance = new JCFUserService();
                }
            }
        }

        return instance;
    }

    @Override
    public User create(String username, String email, String password) {
        User user = new User(username, email, password);
        data.put(user.getId(), user);

        return user;
    }

    @Override
    public User findById(UUID userId) {
        return Optional.ofNullable(data.get(userId))
                .orElseThrow(() -> new NoSuchElementException(userId + " 유저를 찾을 수 없습니다."));
    }

    @Override
    public List<User> findAll() {
        return data.values().stream().toList();
    }

    @Override
    public User update(UUID userId, String newUsername, String newEmail, String newPassword) {
        User user = Optional.ofNullable(data.get(userId))
                .orElseThrow(() -> new NoSuchElementException(userId + " 유저를 찾을 수 없습니다."));
        user.update(newUsername, newEmail, newPassword);

        return user;
    }

    @Override
    public void delete(UUID userId) {
        if (!data.containsKey(userId)) {
            throw new NoSuchElementException(userId + " 유저를 찾을 수 없습니다.");
        }
        data.remove(userId);
    }
}
