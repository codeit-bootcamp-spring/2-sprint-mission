package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {
    private final Map<UUID, User> users = new HashMap<>();
    private static JCFUserService INSTANCE;

    private JCFUserService() {
    }

    public static synchronized JCFUserService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new JCFUserService();
        }
        return INSTANCE;
    }

    @Override
    public void createUser(String username) {
        User user = new User(username);
        users.put(user.getId(), user);
    }

    @Override
    public User getUserById(UUID userId) {
        validateUserExists(userId);
        return users.get(userId);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<User>(users.values());
    }

    @Override
    public String getUserNameById(UUID userId) {
        validateUserExists(userId);
        return users.get(userId).getUsername();
    }

    @Override
    public void updateUsername(UUID userID, String newUsername) {
        User user = getUserById(userID);
        user.updateUsername(newUsername);
    }

    @Override
    public void addChannel(UUID userID, UUID channelId) {
        User user = getUserById(userID);

        if (user.isJoinedChannel(channelId)) {
            throw new IllegalArgumentException("이미 가입되어 있는 채널입니다. ");
        }

        user.addJoinedChannel(channelId);
    }

    @Override
    public void deleteUser(UUID userID) {
        User user = getUserById(userID);
        users.remove(user.getId());
    }

    @Override
    public void deleteChannel(UUID userID, UUID channelId) {
        User user = getUserById(userID);
        user.removeJoinedChannel(channelId);
    }

    public void validateUserExists(UUID userId) {
        if (!users.containsKey(userId)) {
            throw new IllegalArgumentException("존재하지 않는 유저입니다.");
        }
    }
}
