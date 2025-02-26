package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.ArrayDeque;
import java.util.List;
import java.util.UUID;

public class JCFChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;

    public JCFChannelService(ChannelRepository channelRepository, MessageRepository messageRepository) {
        this.channelRepository = channelRepository;
        this.messageRepository = messageRepository;
    }

    @Override
    public void createChannel(String channelName) {
        validateChannelName(channelName);
        Channel newChannel = new Channel(channelName);
        channelRepository.addChannel(newChannel);
    }

    @Override
    public Channel readChannel(UUID channelId) {
        if (channelId == null) {
            throw new IllegalArgumentException("입력받은 channelId 가 null 입니다!!!");
        }
        return channelRepository.findChannelById(channelId);
    }

    @Override
    public ArrayDeque<Channel> readAllChannels() {
        return channelRepository.getChannels();
    }

    @Override
    public List<Message> readMessageListByChannelId(UUID channelId) {
        if (channelId == null) {
            throw new IllegalArgumentException("channelId is null!!!");
        }
        if (!channelRepository.existsById(channelId)) {
            throw new IllegalArgumentException("channelId is not exists!!!");
        }
        return messageRepository.findMessageListByChannelId(channelId);
    }

    @Override
    public void updateChannelName(UUID channelId, String newChannelName) {
        validateChannelName(newChannelName);
        readChannel(channelId).updateChannelName(newChannelName);
    }

    @Override
    public void addChannelParticipant(UUID channelId, User newParticipant) {
        //newParticipant 가 userRepository 에 있는 user 인지는 다른데서 확인 필요?
        channelRepository.addParticipant(channelId, newParticipant);
    }

    @Override
    public void deleteChannel(UUID channelId) {
        if (channelId == null) {
            throw new IllegalArgumentException("삭제하려는 channelId 가 null 입니다!!!");
        }
        channelRepository.deleteChannel(channelId);
    }

    private void validateChannelName(String channelName) {
        if (channelName == null || channelName.trim().isEmpty()) {
            throw new IllegalArgumentException("channelName 은 null 이거나 공백일 수 없다!!!");
        }
    }
}
