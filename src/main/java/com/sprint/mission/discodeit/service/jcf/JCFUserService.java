package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JCFUserService implements UserService {
    private static volatile JCFUserService instance;
    final Map<UUID,User> userRepository = new HashMap<>();
    final Map<String,UUID> userNameToIdRepository = new HashMap<>();

    private JCFUserService() {}

    public static JCFUserService getInstance() {
        if (instance == null) {
            synchronized (JCFUserService.class) {
                if (instance == null) {
                    instance = new JCFUserService();
                }
            }
        }
        return instance;
    }

    @Override
    public void createUser(String userName, Channel channel) {
        if(channel == null) {
            throw new IllegalArgumentException("channel is null");
        }
        if(userNameToIdRepository.containsKey(userName)) {
            throw new IllegalArgumentException("userName " + userName + " already exists");
        }
        User newUser = new User(userName, channel);
        userRepository.put(newUser.getId(), newUser);
        userNameToIdRepository.put(newUser.getUserName(), newUser.getId());
    }

    @Override
    public User findByUserId(UUID userId) {
        return userRepository.get(userId);
    }

    @Override
    public UUID findByUsername(String username) {
        return userNameToIdRepository.get(username);
    }

    @Override
    public User userfindByName(String username) {
        return userRepository.get(userNameToIdRepository.get(username));
    }
}
