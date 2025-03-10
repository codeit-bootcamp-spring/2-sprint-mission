package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class JCFUserRepository implements UserRepository {
    private final Map<UUID, User> userData = new HashMap<>();
    private final Map<String, UUID> userNameToId = new HashMap<>();

    @Override
    public boolean userExists(String userName) {
        return !userNameToId.containsKey(userName);
    }

    @Override
    public User findByName(String userName) {
        return userData.get(userNameToId.get(userName));
    }

    @Override
    public List<User> findAll() {
        if (userData.isEmpty()) {
            throw new IllegalArgumentException("데이터가 존재하지 않습니다.");
        }
        return new ArrayList<>(userData.values());
    }

    @Override
    public List<User> findUpdatedUsers() {
        return userData.values().stream()
                .filter(entry -> entry.getUpdatedAt() != null)
                .collect(Collectors.toList());
    }

    @Override
    public void createUser(String userName, String nickName) {
        User user = new User(userName, nickName);
        UUID uid = user.getId();
        userData.put(uid, user);
        userNameToId.put(userName, uid);
    }

    @Override
    public void updateUser(String oldUserName, String newUserName, String newNickName) {
        UUID uid = userNameToId.get(oldUserName);
        User user = userData.get(uid);
        user.userUpdate(newUserName, newNickName);
        userNameToId.remove(oldUserName);
        userNameToId.put(newUserName, uid);
    }

    @Override
    public void deleteUser(String userName) {
        UUID uid = userNameToId.get(userName);
        userData.remove(uid);
        userNameToId.remove(userName);
    }

}
