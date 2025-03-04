package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.*;

public class UserRepository implements Repository<User> {
    private static volatile UserRepository instance;         // volatile을 사용하여 변수의 값을 JVM이 캐시하지 않도록 보장
    private final Map<UUID, User> users;

    private UserRepository() {
        users = new HashMap<>();
    }

    public static UserRepository getInstance() {
        // 첫 번째 null 체크 (성능 최적화)
        if (instance == null) {
            synchronized (UserRepository.class) {
                // 두 번째 null 체크 (동기화 구간 안에서 중복 생성 방지)
                if (instance == null) {
                    instance = new UserRepository();
                }
            }
        }
        return instance;
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
