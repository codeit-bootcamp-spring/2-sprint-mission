package com.sprint.mission.discodeit.service.jcf;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {
    private final HashMap<UUID, User> users = new HashMap<>();

    private static JCFUserService userService;

    private JCFUserService() {}

    public static JCFUserService getInstance() {
        if(userService == null){
            userService = new JCFUserService();
        }
        return userService;
    }

    @Override
    public void create(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void update(UUID id, String username, String email) {
        User user = this.findById(id).orElseThrow(() -> new RuntimeException("id가 존재하지 않습니다."));

        user.setUpdatedAt(System.currentTimeMillis());
        user.setUsername(username);
        user.setEmail(email);
    }

    @Override
    public void delete(UUID id) {
        if (!users.containsKey(id)) {
            throw new RuntimeException("id가 존재하지 않습니다.");
        }
        users.remove(id);
    }
}
