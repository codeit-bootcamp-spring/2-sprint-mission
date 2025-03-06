package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JCFUserRepository implements UserRepository {

    private final List<User> userList = new ArrayList<>();

    @Override
    public User userSave(String nickname, String password) {
        User user = new User(nickname, password);
        userList.add(user);
        return user;
    }

    @Override
    public Optional<User> findUserById(UUID userUUID) {
        return userList.stream()
                .filter(user -> user.getId().equals(userUUID))
                .findAny();
    }

    @Override
    public List<User> findAllUser() {
        return userList;
    }

    @Override
    public User updateUserNickname(UUID userUUID, String nickname) {
        return userList.stream()
                .filter(user -> user.getId().equals(userUUID))
                .findFirst()
                .map(user -> {
                    user.updateNickname(nickname);
                    return user;
                })
                .orElse(null);
    }

    @Override
    public boolean deleteUserById(UUID userUUID) {
        return userList.removeIf(user -> user.getId().equals(userUUID));
    }
}
