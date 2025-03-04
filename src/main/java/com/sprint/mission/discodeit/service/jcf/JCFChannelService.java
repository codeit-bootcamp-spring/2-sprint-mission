package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCFChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public JCFChannelService(UserRepository userRepository, ChannelRepository channelRepository, MessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.channelRepository = channelRepository;
        this.messageRepository = messageRepository;
    }

    @Override
    public void createChannel(String channelName) {
        Channel newChannel = new Channel(channelName);      //channelName에 대한 유효성 검증은 Channel 생성자에게 맡긴다.
        channelRepository.addChannel(newChannel);
    }

    @Override
    public Channel readChannel(UUID channelId) {
        return this.channelRepository.findChannelById(channelId);
    }

    @Override
    public Map<UUID, Channel> readAllChannels() {
        return channelRepository.getChannels();
    }

    @Override
    public List<Message> readMessageListByChannelId(UUID channelId) {
        return messageRepository.findMessageListByChannelId(channelId);
    }

    @Override
    public void updateChannelName(UUID channelId, String newChannelName) {
        readChannel(channelId).updateChannelName(newChannelName);       //유효한 channelName인지에 대한 검증을 Channel entity의 updateChannelName()에 맡김.
    }

    @Override
    public void addChannelParticipant(UUID channelId, User newParticipant) {        // channelId 검증은 channelRepository 에서 수행
        UserService.validateUserId(newParticipant.getId(), this.userRepository);
        channelRepository.addParticipant(channelId, newParticipant);
    }

    @Override
    public void deleteChannel(UUID channelId) {
        channelRepository.deleteChannel(channelId);
    }
}
