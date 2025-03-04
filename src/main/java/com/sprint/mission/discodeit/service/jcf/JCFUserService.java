package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.exception.DuplicatedUserException;
import com.sprint.mission.discodeit.exception.UserNotFoundException;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {
    private static JCFUserService instance;

    private ChannelService channelService;
    private final Map<UUID, User> users;

    private JCFUserService() {
        users = new HashMap<>();
        channelService = JCFChannelService.getInstance();
    }

    public static JCFUserService getInstance() {
        if (instance == null) {
            instance = new JCFUserService();
        }
        return instance;
    }

    @Override
    public User createUser(String nickname, String email, String avatar, String status) {
        if (hasUserByEmail(email)) {
            throw new DuplicatedUserException("이메일 중복입니다.");
        }
        User user = new User(nickname, email, avatar, status);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User getUserByUserId(UUID userId) {
        validateUserId(userId);
        User user = users.get(userId);
        return user;
    }

    @Override
    public User getUserByEmail(String email) {
        User user = findUserByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("해당 유저가 없습니다.");
        }
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User updateUser(UUID userId, String nickname, String avatar, String status) {
        User user = getUserByUserId(userId);

        user.update(nickname, avatar, status);
        return user;
    }

    @Override
    public void deleteUserByEmail(String email) {
        UUID userId = getUserByEmail(email).getId();
        getChannelService().deleteUserFromEveryChannel(userId);
        users.remove(userId);
    }

    @Override
    public void deleteUserById(UUID userId) {
        validateUserId(userId);
        getChannelService().deleteUserFromEveryChannel(userId);
        users.remove(userId);
    }

    @Override
    public void validateUserId(UUID userId) {
        if (!users.containsKey(userId)) {
            throw new UserNotFoundException("해당 유저가 없습니다.");
        }
    }

    private boolean hasUserByEmail(String email) {
        return findUserByEmail(email) != null;
    }

    private User findUserByEmail(String email) {
        return users.values().stream()
                .filter(u -> email.equals(u.getEmail()))
                .findFirst()
                .orElse(null);
    }

    private ChannelService getChannelService() {
        if (channelService == null) {
            channelService = JCFChannelService.getInstance();
        }
        return channelService;
    }
}
