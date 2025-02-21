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


    public JCFUserService(ChannelService channelService) {
        this.channelService = channelService;
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
        if (!users.containsKey(username)) {
            throw new IllegalArgumentException("존재하지 않는 유저입니다. ");
        }
        return users.get(username);
    }

    @Override
    public List<User> getAllUsers() {
        if (users.isEmpty()) {
            throw new IllegalArgumentException("현재 유저가 없습니다. ");
        }
        return new ArrayList<User>(users.values());
    }

    @Override
    public void updateUsername(User user, String newUsername) {
        if (users.containsKey(newUsername)) {
            throw new IllegalArgumentException("이미 존재하는 유저명입니다.");
        }
        users.remove(user.getUsername());
        user.updateUsername(newUsername);
        users.put(newUsername, user);
    }

    @Override
    public void addChannel(User user, String channel) {
        channelService.validateChannelExists(channel);

        if (!user.isJoinedChannel(channel)) {
            throw new IllegalArgumentException("가입되어있지 않은 채널입니다. ");
        }
        user.updateJoinedChannel(channel);
        users.put(user.getUsername(), user);
    }

    @Override
    public void deleteUser(User user) {
        users.remove(user.getUsername());
    }

    @Override
    public void deleteChannel(User user, String channelName) {
        channelService.validateChannelExists(channelName);

        user.removeJoinedChannel(channelName);
        users.put(user.getUsername(), user);
    }

    public void validateUserExists(String username){
        if (!users.containsKey(username)) {
            throw new IllegalArgumentException("존재하지 않는 유저입니다.");
        }
    }
}
