package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class JCFUserRepository implements UserRepository {
    private final Map<UUID, User> userMap;

    public JCFUserRepository() {
        this.userMap = new HashMap<>();
    }

    @Override
    public User save(User user) {
        this.userMap.put(user.getId(), user);
        return user;
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(userMap.values());
    }

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(userMap.get(id));
    }

    @Override
    public Optional<User> findByUserName(String userName) {
        return userMap.values().stream()
                .filter(user -> user.getUserName().equals(userName))
                .findFirst();
    }

    @Override
    public boolean existsById(UUID id) {
        return this.userMap.containsKey(id);
    }

    @Override
    public void deleteById(UUID id) {
        this.userMap.remove(id);
    }

    @Override
    public boolean existsByUserName(String userName) {
        return userMap.values().stream()
                .anyMatch(user -> user.getUserName().equals(userName));
    }

    @Override
    public boolean existsByUserEmail(String userEmail) {
        return userMap.values().stream()
                .anyMatch(user -> user.getUserEmail().equals(userEmail));
    }
}
