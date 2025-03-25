package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public class JCFUserRepository implements UserRepository {

    private static final Map<UUID, User> userDb = new HashMap<>();

    private static void validUserId(UUID userId) {
        if (!userDb.containsKey(userId)) {
            throw new NoSuchElementException("[ERROR]User ID Error");
        }
    }

    @Override
    public void save(User user) {
        userDb.put(user.getId(), user);
    }

    @Override
    public User findByUserId(UUID userId) {
        return Optional.ofNullable(userDb.get(userId))
            .orElseThrow(
                () -> new IllegalArgumentException("[ERROR]유효하지 않은 아이디 입니다. id : " + userId));
    }

    @Override
    public User findByUserName(String name) {
        for (User user : userDb.values()) {
            if (user.getName().equals(name)) {
                return user;
            }
        }
        throw new IllegalArgumentException("[ERROR] 유효하지 않은 이름 입니다.");
    }

    @Override
    public User findByEmail(String email) {
        for (User user : findAll()) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        throw new IllegalArgumentException("[ERROR] 유효하지 않은 이메일 입니다.");
    }

    @Override
    public List<User> findAll() {
        return userDb.values().stream()
            .sorted(Comparator.comparing(User::getName)).toList();
    }

    @Override
    public void delete(UUID userId) {
        validUserId(userId);
        userDb.remove(userId);
    }

    @Override
    public boolean existsByUsername(String username) {
        return findAll().stream()
            .anyMatch(user -> user.getName().equals(username));
    }

    @Override
    public boolean existByEmail(String email) {
        return findAll().stream()
            .anyMatch(user -> user.getEmail().equals(email));
    }
}
