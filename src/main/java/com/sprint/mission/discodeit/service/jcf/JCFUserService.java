package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import java.util.List;
import java.util.UUID;


public class JCFUserService implements UserService {
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;

    public JCFUserService(UserRepository userRepository, ChannelRepository channelRepository,
                          MessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.channelRepository = channelRepository;
        this.messageRepository = messageRepository;
    }

    @Override
    public User getUser(String userName) {
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
        userRepository.createUser(userName, nickName);
    }

    @Override
    public void updateName(String oldUserName, String newUserName, String newNickName) {
        userRepository.updateUser(oldUserName, newUserName, newNickName);
    }

    @Override
    public void deleteUser(String userName) {
        // 1. userId를 가지는 Channel 리스트 가져오기
        List<UUID> channels = channelRepository.channelListByuserId(userRepository.findByName(userName).getId());
        // 2. userId가지는 유저 삭제
        userRepository.deleteUser(userName);
        // 3. 각 channelId를 가지는 message들 삭제
        for (UUID channelId : channels) {
            messageRepository.deleteMessagesByChannelId(channelId);
        }
        // 4. channels 리스트 삭제 gk...
        channelRepository.deleteChannelList(channels);
    }
}
