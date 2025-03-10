package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.List;
import java.util.UUID;

public class JCFChannelService implements ChannelService {
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;

    public JCFChannelService(UserRepository userRepository, ChannelRepository channelRepository,
                             MessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.channelRepository = channelRepository;
        this.messageRepository = messageRepository;
    }

    @Override
    public Channel getChannel(UUID channelId) {
        if (channelRepository.channelExists(channelId)) {
            throw new IllegalArgumentException("존재하지 않는 채널ID입니다.");
        }
        return channelRepository.findById(channelId);
    }

    @Override
    public List<Channel> getAllChannels() {
        return channelRepository.findAll();
    }

    public List<Channel> getUpdatedChannels() {
        return channelRepository.findUpdatedChannels();
    }

    @Override
    public void registerChannel(String channelName, String userName) {
        if (userRepository.userExists(userName)) {
            throw new IllegalArgumentException("존재하지 않는 사용자명입니다.");
        }
        channelRepository.createChannel(channelName, userRepository.findByName(userName));
    }

    @Override
    public void updateChannel(UUID channelId, String channelName) {
        if (channelRepository.channelExists(channelId)) {
            throw new IllegalArgumentException("존재하지 않는 채널ID입니다.");
        }
        channelRepository.updateChannel(channelId, channelName);
    }

    @Override
    public void deleteChannel(UUID channelId) {
        if (channelRepository.channelExists(channelId)) {
            throw new IllegalArgumentException("존재하지 않는 채널ID입니다.");
        }
        channelRepository.deleteChannel(channelId);
        messageRepository.deleteMessagesByChannelId(channelId);
    }

}
