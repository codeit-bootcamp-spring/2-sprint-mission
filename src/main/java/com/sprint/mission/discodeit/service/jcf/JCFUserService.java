package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;
import java.util.stream.Collectors;

public class JCFUserService implements UserService {
    private static final JCFUserService instance = new JCFUserService();
    private final Map<UUID, User> data = new HashMap<>();

    private JCFUserService() {}

    public static JCFUserService getInstance() {
        return instance;
    }

    @Override
    public User createUser(String name) {
        User user = new User(name);
        data.put(user.getId(), user);  // id를 키로, User 객체를 맵에 저장
        return user;
    }

    @Override
    public Optional<User> getUserById(UUID userid) {
        return Optional.ofNullable(data.get(userid));
    }

    @Override
    public List<User> getUsersByName(String name) {
        return data.values().stream()
                .filter(user -> user.getName().equals(name))  // 이름이 같은 사용자 필터링
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void updateUser(UUID userId, String newName) {
        User user = data.get(userId);  // 해당 Id의 User 객체 가져오기
        if (user != null) {
            user.update(newName);
        }
    }

    @Override
    public void deleteUser(UUID userId) {
        data.remove(userId);
    }

}
