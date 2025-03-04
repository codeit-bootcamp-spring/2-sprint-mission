package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {
    private final Map<UUID, User> users = new HashMap<>();
    private final Map<String, UUID> userIds = new HashMap<>();
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
    public void updataUserData() {
    }

    @Override
    public void createUser(String username) {
        if (userIds.containsKey(username)) {
            throw new IllegalArgumentException("이미 존재하는 유저입니다.");
        }
        User user = new User(username);
        users.put(user.getId(), user);
        userIds.put(username, user.getId());
    }

    @Override
    public User getUserByName(String username) {
        validateUserExists(username);
        return users.get(userIds.get(username));
    }

    @Override
    public User getUserById(UUID userId) {
        validateUserExists(userId);
        return users.get(userId);
    }

    @Override
    public UUID getUserIdByName(String username) {
        validateUserExists(username);
        return userIds.get(username);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<User>(users.values());
    }

    @Override
    public String getUserNameByid(UUID userId) {
        validateUserExists(userId);
        return users.get(userId).getUsername();
    }

    @Override
    public void updateUsername(UUID userID, String newUsername) {
        if (userIds.containsKey(newUsername)) {
            throw new IllegalArgumentException("이미 존재하는 유저명입니다.");
        }
        User user = getUserById(userID);
        String oldUserName = user.getUsername();
        user.updateUsername(newUsername);
        userIds.put(newUsername, userIds.remove(oldUserName));
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
        userIds.remove(user.getUsername());
    }

    @Override
    public void deleteChannel(UUID userID, UUID channelId) {
        User user = getUserById(userID);
        user.removeJoinedChannel(channelId);
    }

    public void validateUserExists(String username) {
        UUID userId = userIds.get(username);
        if (userId == null) {
            throw new IllegalArgumentException("존재하지 않는 유저입니다.");
        }
        validateUserExists(userId);
    }

    public void validateUserExists(UUID userId) {
        if (!users.containsKey(userId)) {
            throw new IllegalArgumentException("존재하지 않는 유저입니다.");
        }
    }
}
