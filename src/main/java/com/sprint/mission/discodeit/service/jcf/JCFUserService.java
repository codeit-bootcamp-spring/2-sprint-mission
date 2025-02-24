package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCFUserService implements UserService {
    private final Map<UUID, User> userDb = new HashMap<>();

    public JCFUserService() {}

    @Override
    public void create(User user) {
        if (userDb.containsKey(user.getId())) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }
        userDb.put(user.getId(), user);
        System.out.println("["+ user.getId() + "] 생성이 완료되었습니다.");
    }

    @Override
    public User find(UUID id) {
        if (!userDb.containsKey(id)) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }
        return userDb.get(id);
    }

    @Override
    public List<User> findAll() {
        return userDb.values().stream().toList();
    }

    @Override
    public void update(UUID id, User updateUser) {
        if (userDb.containsKey(id)) {
            User existingUser = userDb.get(id);
            existingUser.update(updateUser.getName(), updateUser.getAge());
        }
    }

    @Override
    public void delete(UUID id) {
        userDb.remove(id);
    }
}
