package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.*;

public class UserRepository implements Repository<User> {
    private final Map<UUID, User> users = new HashMap<>();

    private static class SingletonHolder {
        private static final UserRepository INSTANCE = new UserRepository();
    }

    private UserRepository() {}

    public static UserRepository getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    public void add(User newUser) {
        if (newUser == null) {
            throw new IllegalArgumentException("newUser 는 null 이 될 수 없습니다!!!");
        }
        this.users.put(newUser.getId(), newUser);
    }

    @Override
    public boolean existsById(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("null값을 가지는 userId가 들어왔습니다!!!");
        }
        return users.containsKey(userId);
    }

    @Override
    public User findById(UUID userId) {
        if (!existsById(userId)) {
            throw new NoSuchElementException("해당 userId를 가진 사용자를 찾을 수 없습니다 : " + userId);
        }
        return this.users.get(userId);
    }

    @Override
    public Map<UUID, User> getAll() {
        return this.users;
    }

    @Override
    public void deleteById(UUID userId) {
        if (!existsById(userId)) {
            throw new NoSuchElementException("해당 userId를 가진 사용자를 찾을 수 없습니다 : " + userId);
        }
        this.users.remove(userId);
    }
}
