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
        data.put(user.getId(), user);
    }

    @Override
    public Optional<User> findById(UUID userId) {

        return Optional.ofNullable(data.get(userId));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(data.values());

    }

    @Override
    public void delete(UUID id) {
        data.remove(id);
    }

    @Override
    public void update(UUID id, String nickname) {
        if (data.containsKey(id)) {
            User user = data.get(id);
            user.setNickname(nickname, System.currentTimeMillis());
        }
    }
}
