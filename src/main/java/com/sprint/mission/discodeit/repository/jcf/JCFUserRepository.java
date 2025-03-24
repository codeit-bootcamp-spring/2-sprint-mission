package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.*;

public class JCFUserRepository implements UserRepository {
    private final Map<UUID, User> userData = new HashMap<>();


    @Override
    public User save(User user) {
        userData.put(user.getId(), user);
        return  user;
    }

    @Override
    public User findById(UUID id) {
        return userData.get(id);
    }

    @Override
    public List<User> findAll() {
        return userData.values().stream().toList();
    }

    @Override
    public User update(UUID id, String newUsername, String newEmail, String newPassword) {
        User userNullable = userData.get(id);
        User user = Optional.ofNullable(userNullable).orElseThrow(() -> new NoSuchElementException(id + "가 존재하지 않습니다."));
        user.updateUser(newUsername, newEmail, newPassword);

        return user;
    }

    @Override
    public void delete(UUID id) {
        userData.remove(id);
    }
}
