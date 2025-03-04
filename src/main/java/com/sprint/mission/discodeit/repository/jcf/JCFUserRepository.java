package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.*;

public class JCFUserRepository implements UserRepository {

    private static volatile JCFUserRepository instance;
    private final Map<UUID, User> data;

    private JCFUserRepository() {
        this.data = new HashMap<>();
    }

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

    @Override
    public void save(User user) {
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
