package com.sprint.mission.discodeit.jcf;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {
    HashMap<UUID, User> users = new HashMap<>();

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
    public void update(UUID id) {
        Optional<User> user = this.findById(id);

        if (user.isEmpty()) {
            throw new RuntimeException("id가 존재하지 않습니다.");
        }

        user.ifPresent(ch -> ch.setUpdatedAt(System.currentTimeMillis()));
    }

    @Override
    public void delete(UUID id) {
        users.remove(id);
    }
}
