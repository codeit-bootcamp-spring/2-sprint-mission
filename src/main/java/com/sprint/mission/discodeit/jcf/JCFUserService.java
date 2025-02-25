package com.sprint.mission.discodeit.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JCFUserService implements UserService {
    private final Map<String, User> users = new HashMap<String, User>();
    private final ChannelService channelService;
    private static JCFUserService INSTANCE;

    private JCFUserService(ChannelService channelService) {
        this.channelService = channelService;
    }

    public static JCFUserService getInstance(ChannelService channelService) {
        if (INSTANCE == null) {
            INSTANCE = new JCFUserService(channelService);
        }
        return INSTANCE;
    }

    @Override
    public void createUser(String username) {
        if (users.containsKey(username)) {
            throw new IllegalArgumentException("이미 존재하는 유저입니다.");
        }
        users.put(username, new User(username));
    }

    @Override
    public User getUser(String username) {
        validateUserExists(username);
        return users.get(username);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<User>(users.values());
    }

    @Override
    public void updateUsername(User user, String newUsername) {
        if (users.containsKey(newUsername)) {
            throw new IllegalArgumentException("이미 존재하는 유저명입니다.");
        }

        String oldUserName = user.getUsername();
        user.updateUsername(newUsername);
        users.put(newUsername, users.remove(oldUserName));
    }

    @Override
    public void addChannel(User user, String channel) {
        channelService.validateChannelExists(channel);
        if (user.isJoinedChannel(channel)) {
            throw new IllegalArgumentException("이미 가입되어 있는 채널입니다. ");
        }
        user.addJoinedChannel(channel);
    }

    @Override
    public void deleteUser(User user) {
        users.remove(user.getUsername());
    }

    @Override
    public void deleteChannel(User user, String channelName) {
        channelService.validateChannelExists(channelName);

        user.removeJoinedChannel(channelName);
    }

    public void validateUserExists(String username){
        if (!users.containsKey(username)) {
            throw new IllegalArgumentException("존재하지 않는 유저입니다.");
        }
    }
}
