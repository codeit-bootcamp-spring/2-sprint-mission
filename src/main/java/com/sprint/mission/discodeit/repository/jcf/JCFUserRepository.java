package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
@Repository
public class JCFUserRepository implements UserRepository {

    private final Map<UUID, User> userData;

    public JCFUserRepository() {
        this.userData = new HashMap<>();
    }

    @Override
    public User save(User user) {
        userData.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(UUID userId) {
        return Optional.ofNullable(this.userData.get(userId));
    }

    @Override
    public Optional<User> findByUserName(String userName) {
        return userData.values().stream()
                .filter(user -> user.getUserName().equals(userName)) // username 비교
                .findFirst();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userData.values().stream()
                .filter(user -> user.getEmail().equals(email)) // email 비교
                .findFirst();
    }

    @Override
    public List<User> findAll() {
        return this.userData.values().stream().toList();
    }


    @Override
    public boolean existsById(UUID userId) {
        return userData.containsKey(userId);
    }

    @Override
    public void deleteById(UUID userId) {
        this.userData.remove(userId);
    }

    @Override
    public boolean existsByEmail(String email) {
        return this.findAll().stream().anyMatch(user -> user.getEmail().equals(email));
    }

    @Override
    public boolean existsByUsername(String username) {
        return this.findAll().stream().anyMatch(user -> user.getUserName().equals(username));
    }
}
