package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.*;

public class UserRepository {
    private final Set<User> users = new HashSet<>();

    private static class SingletonHolder {
        private static final UserRepository INSTANCE = new UserRepository();
    }

    private UserRepository() {}

    public static UserRepository getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public Set<User> getUsers() {
        return this.users;
    }

    public void addUser(User newUser) {
        if (newUser == null) {
            throw new IllegalArgumentException("newUser 는 null 이 될 수 없습니다!!!");
        }
        this.users.add(newUser);        // Set 은 add 과정에서 중복을 처리한다.이때 내부적으로 equals()와 hashcode()를 활용하기 때문에
    }                                   // User 클래스에서 equals()와 hashcode()를 오버라이드 하면 중복을 처리하며 add를 수행한다.

    public User findUserById(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("null값을 가지는 userId가 들어왔습니다!!!");
        }
        return  this.users.stream()
                    .filter(user -> Objects.equals(user.getId(), userId))
                    .findFirst().orElseThrow(() -> new IllegalArgumentException("해당 userId를 가진 User 를 찾을 수 없습니다!!!"));
    }

    // id가 유효하지 않을 때의 예외처리?
    public void deleteUser(UUID userId) {
        boolean removed = this.users.removeIf(user -> Objects.equals(user.getId(), userId));
        if (!removed) {
            throw new IllegalArgumentException("삭제할 id를 가진 사용자가 존재하지 않습니다!!!");
        }
    }
}
