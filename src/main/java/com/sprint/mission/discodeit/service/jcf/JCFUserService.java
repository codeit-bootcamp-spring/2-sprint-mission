package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.dto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.UpdateDefinition;

import java.util.*;

public class JCFUserService implements UserService {
    private final Map<UUID, User> data;

    public JCFUserService() {
        this.data = new HashMap<>();
    }

    @Override
    public User create(UserCreateRequest createDefintion, UUID newProfileId) {
        User user = new User(createDefintion.getUsername(), createDefintion.getEmail(), createDefintion.getPassword());
        this.data.put(user.getId(), user);

        return user;
    }

    @Override
    public User find(UUID userId) {
        User userNullable = this.data.get(userId);

        return Optional.ofNullable(userNullable)
                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
    }

    @Override
    public List<User> findAll() {
        return this.data.values().stream().toList();
    }

    @Override
    public User update(UUID userId, UpdateDefinition updateDefinition) { // ✅ 인터페이스와 일치하도록 수정
        User userNullable = this.data.get(userId);
        User user = Optional.ofNullable(userNullable)
                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));

        user.update(updateDefinition.getUsername(), updateDefinition.getEmail(), updateDefinition.getPassword());
        return user;
    }


    @Override
    public void delete(UUID userId) {
        if (!this.data.containsKey(userId)) {
            throw new NoSuchElementException("User with id " + userId + " not found");
        }
        this.data.remove(userId);
    }
}
