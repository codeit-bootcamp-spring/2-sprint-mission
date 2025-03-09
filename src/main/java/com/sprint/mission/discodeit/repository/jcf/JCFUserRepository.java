package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.*;

public class JCFUserRepository implements UserRepository {

    private final Map<UUID,User> userdata;
    private static JCFUserRepository instance = null;

    public static JCFUserRepository getInstance() {
        if (instance == null) {
            instance = new JCFUserRepository();
        }
        return instance;
    }

    private JCFUserRepository() {
        this.userdata=new HashMap<>();
    }

    @Override
    public void save(User user) {
        userdata.put(user.getId(), user);
    }

    @Override
    public User findById(UUID id) {
        return userdata.get(id);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(userdata.values());
    }

    @Override
    public void delete(UUID id) {
        userdata.remove(id);
    }

    @Override
    public void update(User user) {
        user.updateTime(System.currentTimeMillis());
        userdata.put(user.getId(), user);
    }

    @Override
    public boolean existsById(UUID id) {
        return userdata.containsKey(id);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userdata.values().stream()
                .anyMatch(user -> user.getUsername().equals(username));
    }
}
