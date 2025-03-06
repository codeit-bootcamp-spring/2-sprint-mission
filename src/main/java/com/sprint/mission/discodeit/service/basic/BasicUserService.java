package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import java.util.List;
import java.util.UUID;

public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;

    public BasicUserService(UserRepository userRepository, ChannelRepository channelRepository,
                            MessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.channelRepository = channelRepository;
        this.messageRepository = messageRepository;
    }

    @Override
    public User getUser(String userName) {
        if (userRepository.userExists(userName)) {
            throw new IllegalArgumentException("존재하지 않는 사용자명입니다.");
        }
        return userRepository.findByName(userName);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<User> getUpdatedUsers() {
        return userRepository.findUpdatedUsers();
    }

    @Override
    public void registerUser(String userName, String nickName) {
        if (!userRepository.userExists(userName)) {
            throw new IllegalArgumentException("존재하는 사용자명입니다.");
        }
        userRepository.createUser(userName, nickName);
    }

    @Override
    public void updateName(String oldUserName, String newUserName, String newNickName) {
        if (userRepository.userExists(oldUserName)) {
            throw new IllegalArgumentException("존재하지 않는 사용자명입니다.");
        }
        userRepository.updateUser(oldUserName, newUserName, newNickName);
    }

    @Override
    public void deleteUser(String userName) {
        if (userRepository.userExists(userName)) {
            throw new IllegalArgumentException("존재하지 않는 사용자명입니다.");
        }
        List<UUID> channels = channelRepository.channelListByuserId(userRepository.findByName(userName).getId());
        userRepository.deleteUser(userName);
        for (UUID channelId : channels) {
            messageRepository.deleteMessagesByChannelId(channelId);
        }
        channelRepository.deleteChannelList(channels);
    }
}
