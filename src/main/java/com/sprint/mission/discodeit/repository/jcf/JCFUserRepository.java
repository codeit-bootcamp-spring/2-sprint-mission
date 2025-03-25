package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@ConditionalOnProperty(name = "repository.type", havingValue = "jcf", matchIfMissing = true)
public class JCFUserRepository implements UserRepository {
    private final Map<UUID, User> data;

    public JCFUserRepository() {
        this.data = new HashMap<>();
    }

    @Override
    public User save(User user) {
        this.data.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(UUID userId) {
        User userNullable = this.data.get(userId);
        return Optional.ofNullable(Optional.ofNullable(userNullable)
                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found")));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(this.data.values());
    }

    @Override
    public User update(User user) {
        if (!this.data.containsKey(user.getId())) {
            throw new NoSuchElementException("User with id " + user.getId() + " not found");
        }
        this.data.put(user.getId(), user);
        return user;
    }

    @Override
    public void delete(UUID userId) {
        if (!this.data.containsKey(userId)) {
            throw new NoSuchElementException("User with id " + userId + " not found");
        }
        this.data.remove(userId);
    }

    @Override
    public boolean exists(UUID userId) {
        return this.data.containsKey(userId);
    }

    @Override
    public boolean existsByUsername(String username) {
        for (User user : this.data.values()) {
            if ((user.getUsername().equals(username))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean existsByEmail(String email) {
        for (User user : this.data.values()) {
            if ((user.getEmail().equals(email))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean existsByUsernameOrEmail(String username, String email) {
        for (User user : this.data.values()) {
            if ((user.getUsername().equals(username)) || (user.getEmail().equals(email))) {
                return true;
            }
        }
        return false;
    }

}
