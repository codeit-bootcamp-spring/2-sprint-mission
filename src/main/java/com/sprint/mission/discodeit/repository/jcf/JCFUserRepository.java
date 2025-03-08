package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.*;

public class JCFUserRepository implements UserRepository {

    private static final Map<UUID, User> users = new HashMap<>();

    @Override
    public void save(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public User findById(UUID userId) {
        validateUserExists(userId);
        return users.get(userId);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void deleteById(UUID userId) {
        validateUserExists(userId);
        users.remove(userId);
    }

    @Override
    public boolean exists(UUID userId) {
        return users.containsKey(userId);
    }

    private void validateUserExists(UUID userId) {
        if (!exists(userId)) {
            throw new IllegalArgumentException("존재하지 않는 유저입니다.");
        }
    }
}
