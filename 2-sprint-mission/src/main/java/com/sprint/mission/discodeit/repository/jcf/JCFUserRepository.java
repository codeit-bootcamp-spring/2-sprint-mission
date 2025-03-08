package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserRole;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.*;

public class JCFUserRepository implements UserRepository {

    private final Map<UUID, User> data = new HashMap<>();

    @Override
    public void createUser(User user) {
        data.put(user.getId(), user);
    }

    @Override
    public Optional<User> selectUserById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public Optional<User> selectUserByNickname(String nickname) {
        return data.values().stream()
                .filter(user -> user.getNickname().equals(nickname))
                .findFirst();
    }

    @Override
    public Optional<User> selectUserByEmail(String email) {
        return data.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public List<User> selectAllUsers() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void updateUser(UUID id, String password, String nickname, UserStatus status, UserRole role) {
        User user = data.get(id);

        user.updatePassword(password);
        user.updateNickname(nickname);
        user.updateStatus(status);
        user.updateRole(role);
    }

    @Override
    public void deleteUser(UUID id) {
        data.remove(id);
    }

    @Override
    public void clearUsers() {
        data.clear();
    }
}
